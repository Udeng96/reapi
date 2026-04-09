import {createAction} from "@reduxjs/toolkit";


const types ={
    FETCH_ADDR : `FETCH_ADDR`
}

const actions = {
    fetchAddr : createAction<{word:string, type:string }>(types.FETCH_ADDR)
}

export {types, actions};