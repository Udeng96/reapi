import {createAction} from "@reduxjs/toolkit";

const types={
    FETCH_DISPATCH_FIRE_CAR : `FETCH_DISPATCH_FIRE_CAR`,
}

const actions={
    fetchDispatchFireCar : createAction<string>(types.FETCH_DISPATCH_FIRE_CAR),

}

export {types,actions}