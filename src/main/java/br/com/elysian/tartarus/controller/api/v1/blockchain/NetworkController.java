package br.com.elysian.tartarus.controller.api.v1.blockchain;

import br.com.elysian.tartarus.dto.v1.SuccessDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.NetworkDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.SavePrivateKeyDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.CompanyNetwork;
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
    public ResponseEntity<NetworkDTO> getNetwork() throws CompanyNotLoggedInException, NetworkNotLoggedInException, NetworkNotFoundException {
        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();

        return ResponseEntity.ok(
                new NetworkDTO(companyNetwork.getNetwork())
        );
    }


    @GetMapping("/registered-networks")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<ArrayList<NetworkDTO>> getRegisteredNetworks() {
        ArrayList<NetworkDTO> networks = networkService.getRegisteredNetworks();
        return ResponseEntity.ok(networks);
    }


    @GetMapping("/available-networks")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<ArrayList<NetworkDTO>> getAvailableNetworks() {
        ArrayList<NetworkDTO> networks = networkService.getAvailableNetworks();
        return ResponseEntity.ok(networks);
    }


    @PostMapping("/with")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TokenDTO> networkWith(@RequestParam("tag") String networkTag, HttpServletResponse response) throws NetworkNotFoundException, InvalidUsernameOrPasswordException {
        // TODO: Implementation for validation if account is registered in network selected!!
        networkService.with(networkTag);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @PostMapping("/save-private-key")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<SuccessDTO> savePrivateKey(@Valid @RequestBody SavePrivateKeyDTO savePrivateKeyDTO) throws NetworkNotLoggedInException, CompanyNotLoggedInException, AccountNotRegisteredInCompanyNetworkException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NetworkNotFoundException {
        return ResponseEntity.ok(
                networkService.savePrivateKey(savePrivateKeyDTO)
        );
    }

}
