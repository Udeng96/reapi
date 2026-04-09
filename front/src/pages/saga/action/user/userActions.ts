import {createAction} from "@reduxjs/toolkit"

const types = {
    REQUEST_USER : `REQUEST_USER`
}

const actions = {
    requestUser : createAction(types.REQUEST_USER)
}



export {types , actions};