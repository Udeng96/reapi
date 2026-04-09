import React from "react";

const CctvPopup : React.FC = () => {
    return(
        <>
            <section className="cctv_full_popup">
                <header className="cctv_full_popup_header">
                    <p className="cctv_full_title type_event">CCTV 001_화성_기안동_SingleFTM_V1</p>
                    {/* <p className="cctv_full_title type_castnet">CCTV 001_화성_기안동_SingleFTM_V1</p> */}
                    <button type="button" className="btn_close"/>
                </header>
                <div className="cctv_in"/>
            </section>
        </>
    )
}

export default CctvPopup