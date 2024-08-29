package br.com.elysian.tartarus.controller.api.v1.blockchain;

import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.NetworkDTO;
import br.com.elysian.tartarus.exception.CompanyNotLoggedInException;
import br.com.elysian.tartarus.exception.InvalidUsernameOrPasswordException;
import br.com.elysian.tartarus.exception.NetworkNotFoundException;
import br.com.elysian.tartarus.exception.NetworkNotLoggedInException;
import br.com.elysian.tartarus.model.CompanyNetwork;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.blockchain.NetworkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @GetMapping()
    public ResponseEntity<NetworkDTO> getNetwork() throws CompanyNotLoggedInException, NetworkNotLoggedInException, NetworkNotFoundException {
        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();

        return ResponseEntity.ok(
                new NetworkDTO(companyNetwork.getNetwork())
        );
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @GetMapping("/registered-networks")
    public ResponseEntity<ArrayList<NetworkDTO>> getRegisteredNetworks() {
        ArrayList<NetworkDTO> networks = networkService.getRegisteredNetworks();
        return ResponseEntity.ok(networks);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @GetMapping("/available-networks")
    public ResponseEntity<ArrayList<NetworkDTO>> getAvailableNetworks() {
        ArrayList<NetworkDTO> networks = networkService.getAvailableNetworks();
        return ResponseEntity.ok(networks);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @PostMapping("/with")
    public ResponseEntity<TokenDTO> networkWith(@RequestParam("tag") String networkTag, HttpServletResponse response) throws NetworkNotFoundException, InvalidUsernameOrPasswordException {
        // TODO: Implementation for validation if account is registered in network selected!!
        networkService.with(networkTag);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }

}
