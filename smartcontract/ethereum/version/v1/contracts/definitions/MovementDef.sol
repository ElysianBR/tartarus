// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.21;

/**
 * @title Movement
 */
    struct Movement {
        uint256 movementId;
        uint256 productId;
        string unitCode;
        uint256 amount;
        address from;
        address to;
        uint256 timestamp;
    }
