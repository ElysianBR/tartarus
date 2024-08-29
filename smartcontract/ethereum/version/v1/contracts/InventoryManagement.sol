// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.21;

import "./SupplyChainBase.sol";
import "./definitions/StockDef.sol";

contract InventoryManagement is SupplyChainBase {
    mapping(uint => mapping(string => Stock)) private stock;

    function increaseStock(uint productId, string calldata unitCode, uint amount) public chargeFee isAdmin consumerHasAccess {
        stock[productId][unitCode].amount += amount;
    }

    function decreaseStock(uint productId, string calldata unitCode, uint amount) public chargeFee isAdmin consumerHasAccess {
        require(stock[productId][unitCode].amount >= amount, "Not enough stock");
        stock[productId][unitCode].amount -= amount;
    }

    function getStock(uint productId, string calldata unitCode) public view isAdmin consumerHasAccess returns (Stock memory) {
        return stock[productId][unitCode];
    }
}
