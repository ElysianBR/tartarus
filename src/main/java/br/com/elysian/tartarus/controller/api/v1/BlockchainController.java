package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.GetStockIndexDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.StockIndexDTO;
import br.com.elysian.tartarus.model.Blockchain;
import br.com.elysian.tartarus.model.CompanyBlockchain;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.blockchain.BlockchainService;
import br.com.elysian.tartarus.service.blockchain.ethereum.contract.StockContract;
import br.com.elysian.tartarus.service.blockchain.ethereum.client.SepoliaClient;

import br.com.elysian.tartarus.service.blockchain.ethereum.model.contract.stock.Stock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @PostMapping("/swith")
    public ResponseEntity<TokenDTO> swith(@RequestParam("tag") String tag, HttpServletResponse response) {
        // TODO: Implementar camada de seguranaça para fazer o set da credencial DENTREO do AuthService ou de BlockchainService, fazendo a validação se de fato o usuário possui acesso a blockchain.
        AuthenticationAccount authenticationAccount = authService.getCurrentAuthentication();
        ((CredentialAccount) authenticationAccount.getCredentials()).setBlockchainTag(tag);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @GetMapping("/getStockIndex")
    public ResponseEntity<StockIndexDTO> getStockIndex(@RequestParam(value = "adress", required = false) String adress, @Valid @RequestBody GetStockIndexDTO getStockIndexdto) throws Exception {
        CompanyBlockchain companyBlockchain = blockchainService.getBlockchainLoggedIn();

        if (companyBlockchain != null) {

            SepoliaClient client;
            if (adress != null) {
                client = new SepoliaClient(adress);
            } else {
                client = new SepoliaClient(companyBlockchain.getAdress());
            }

            //TODO: Implementar os limites de gas dentro da tabela CompanyBlockchain
            BigInteger maxGas = Convert.toWei(BigDecimal.valueOf(60), Convert.Unit.GWEI).toBigInteger();


            StockContract stockContract = new StockContract(client, maxGas);
            Stock stock = stockContract.getStock();

            BigInteger stockIndex = stock.findStockIndex(new Stock.StockPksDef(getStockIndexdto.getProductId(), getStockIndexdto.getControl())).send();

            return ResponseEntity.ok(new StockIndexDTO(stockIndex));
        }

        throw new Exception("Blockchain not logged.");
    }


}
