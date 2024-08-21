package br.com.elysian.tartarus.controller.api.v1.blockchain;

import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.GetStockIndexDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.StockIndexDTO;
import br.com.elysian.tartarus.exception.BlockchainNotFoundException;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.blockchain.BlockchainService;
import br.com.elysian.tartarus.service.blockchain.ethereum.EtherClient;
import br.com.elysian.tartarus.service.blockchain.ethereum.contract.StockContract;

import br.com.elysian.tartarus.service.blockchain.ethereum.contract.model.Stock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/blochain")
public class BlockchainController {

    private final AuthService authService;
    private final BlockchainService blockchainService;

    @Autowired
    public BlockchainController(AuthService authService, BlockchainService blockchainService) {
        this.authService = authService;
        this.blockchainService = blockchainService;
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @PostMapping("/swith")
    public ResponseEntity<TokenDTO> swith(@RequestParam("tag") String blockchainTag, HttpServletResponse response) throws BlockchainNotFoundException {
        blockchainService.swith(blockchainTag);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @PostMapping("/getStockIndex")
    public ResponseEntity<StockIndexDTO> getStockIndex(@Valid @RequestBody GetStockIndexDTO getStockIndexdto) throws Exception {
        EtherClient client = blockchainService.getEtherClientLoggedIn();

        StockContract stockContract = new StockContract(client);
        Stock stock = stockContract.getStock();

        BigInteger stockIndex = stock.findStockIndex(new Stock.StockPksDef(getStockIndexdto.getProductId(), getStockIndexdto.getControl())).send();

        return ResponseEntity.ok(new StockIndexDTO(stockIndex));
    }


}
