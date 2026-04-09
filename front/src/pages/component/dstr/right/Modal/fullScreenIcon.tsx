import React from "react";

const FullScreenIcon = (props:{clickFn :Function}) => {
    return (
        <button type="button" className="btn_cctv_full"
                onClick={()=>props.clickFn()}
        />
    )
}

FullScreenIcon.defaultProps={
    clickFn : ()=>{}
}

export default FullScreenIcon