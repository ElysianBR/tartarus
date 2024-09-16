package br.com.elysian.tartarus.controller.api.v1.blockchain;

import br.com.elysian.tartarus.dto.v1.SuccessDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.*;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.model.Network;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.blockchain.NetworkService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/blockchain/network")
public class NetworkController {

    private final AuthService authService;
    private final NetworkService networkService;

    @Autowired
    public NetworkController(AuthService authService, NetworkService networkService) {
        this.authService = authService;
        this.networkService = networkService;
    }


    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<Network> getNetwork() throws CompanyNotLoggedInException, NetworkNotLoggedInException, NetworkNotFoundException {
        return ResponseEntity.ok(
                networkService
                        .getCompanyNetworkLoggedIn()
                        .getNetwork()
        );
    }


    @GetMapping("/registered-networks")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<ArrayList<Network>> getRegisteredNetworks() {
        return ResponseEntity.ok(
                networkService.getRegisteredNetworks()
        );
    }


    @GetMapping("/available-networks")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<Set<Network>> getAvailableNetworks() {
        return ResponseEntity.ok(
                networkService.getAvailableNetworks()
        );
    }


    @PatchMapping()
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TokenDTO> networkWith(@RequestParam("tag") String networkTag, HttpServletResponse response) throws NetworkNotFoundException, InvalidUsernameOrPasswordException, CompanyNotLoggedInException {
        networkService.patch(networkTag);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }

    @PatchMapping("/private-key")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<SuccessDTO> putPrivateKey(@Valid @RequestBody SavePrivateKeyDTO savePrivateKeyDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NetworkNotFoundException {
        return ResponseEntity.ok(
                networkService.savePrivateKey(savePrivateKeyDTO)
        );
    }

}
