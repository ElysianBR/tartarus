// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.21;

contract SupplyChainBase {
    address private superAdmin;
    address private feeRecipient = 0x612f733F3809D2868EAbC02F675AA173c23dB548;
    mapping(address => bool) private admins;
    mapping(address => mapping(string => bool)) private consumersAccess;
    mapping(string => bool) public availableAccess;
    mapping(bytes4 => string) public sigAccess;


    constructor() {
        superAdmin = msg.sender;
        admins[msg.sender] = true;

        // Registre all access;
        availableAccess["increaseStock"] = true;
        availableAccess["decreaseStock"] = true;
        availableAccess["getStock"] = true;
        availableAccess["recordMovement"] = true;
        availableAccess["getMovementHistory"] = true;
        availableAccess["addProduct"] = true;
        availableAccess["getProduct"] = true;

        sigAccess[bytes4(keccak256("increaseStock(uint256,string,uint)"))] = "increaseStock";
        sigAccess[bytes4(keccak256("decreaseStock(uint256,string,uint)"))] = "decreaseStock";
        sigAccess[bytes4(keccak256("getStock(uint256,string)"))] = "getStock";
        sigAccess[bytes4(keccak256("recordMovement(uint256,string,uint,address,address)"))] = "recordMovement";
        sigAccess[bytes4(keccak256("getMovementHistory(uint256)"))] = "getMovementHistory";
        sigAccess[bytes4(keccak256("addProduct(uint256,Product)"))] = "addProduct";
        sigAccess[bytes4(keccak256("getProduct(uint256)"))] = "getProduct";
    }


    modifier isSuperAdmin() {
        require(superAdmin == msg.sender, "Not is Super Admin");
        _;
    }


    modifier isAdmin() {
        require(admins[msg.sender], "Not an Admin");
        _;
    }


    modifier consumerHasAccess() {
        string memory accessStr = sigAccess[msg.sig];
        require(consumersAccess[msg.sender][accessStr], "Consumer does not have access");
        _;
    }


    modifier chargeFee() {
        uint256 initialGas = gasleft(); // Capture o gas restante antes da execução
        _; // Executa a função
        uint256 gasUsed = initialGas - gasleft(); // Calcule o gas usado
        uint256 fee = (gasUsed * tx.gasprice * 1) / 100; // 2.5% de taxa

        require(msg.sender.balance >= fee, "Insufficient balance for fee");
        payable(feeRecipient).transfer(fee); // Transferir a taxa
    }


    function registerConsumersAccess(address consumersAdress, string calldata access) public isAdmin {
        require(availableAccess[access], "Access does not exist");
        consumersAccess[consumersAdress][access] = true;
    }


    function unregisterConsumersAccess(address consumersAdress, string calldata access) public isAdmin {
        consumersAccess[consumersAdress][access] = false;
    }


    function addAdmin(address adminAddress) public isSuperAdmin {
        admins[adminAddress] = true;
    }


    function removeAdmin(address adminAddress) public isSuperAdmin {
        admins[adminAddress] = false;
    }
}
