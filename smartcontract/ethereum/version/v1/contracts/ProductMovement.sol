// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.21;

import "./SupplyChainBase.sol";
import "./definitions/MovementDef.sol";

contract ProductMovement is SupplyChainBase {

    Movement[] private movements;

    function recordMovement(uint productId, string calldata unitCode, uint amount, address from, address to) public chargeFee isAdmin consumerHasAccess {
        movements.push(Movement(movements.length, productId, unitCode, amount, from, to, block.timestamp));
    }

    function getMovementHistory(uint productId) public view isAdmin consumerHasAccess returns (Movement[] memory) {
        uint count = 0;
        for (uint i = 0; i < movements.length; i++) {
            if (movements[i].productId == productId) count++;
        }

        Movement[] memory result = new Movement[](count);
        uint index = 0;
        for (uint i = 0; i < movements.length; i++) {
            if (movements[i].productId == productId) {
                result[index] = movements[i];
                index++;
            }
        }
        return result;
    }
}
