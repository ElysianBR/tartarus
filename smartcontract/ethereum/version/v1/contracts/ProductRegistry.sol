// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.21;

import "./SupplyChainBase.sol";
import "./definitions/ProductDef.sol";

contract ProductRegistry is SupplyChainBase {

    mapping(address => mapping(string => Product)) private products;

    function addProduct(Product calldata product) external chargeFee isAdmin consumerHasAccess {
        require(
            bytes(products[msg.sender][product.id].id).length == 0,
            "Product already registered"
        );

        products[msg.sender][product.id] = product;
    }

    function getProduct(string calldata id) public view isAdmin consumerHasAccess returns (Product memory) {
        return products[msg.sender][id];
    }

}
