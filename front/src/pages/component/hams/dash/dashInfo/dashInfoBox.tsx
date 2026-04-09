import {HAMS_DASH_INFO_TYPE} from "../../../../../data/const/hamsConst";
import {MutableRefObject, useEffect, useRef, useState} from "react";

const DashInfoBox = (props: { id: string, nm: string, value: string }) => {

    let lat = ""
    let lon = ""

    const addrRef: MutableRefObject<any> = useRef();
    const [showTooltip, setShowTooltip] = useState<boolean>(false);

    useEffect(() => {
        setShowTooltip(false)
    }, [])


    if (props.id === HAMS_DASH_INFO_TYPE[2].id) {
        lat = props.value.split(',')[0]
        lon = props.value.split(',')[1]
    }

    return (
        <div className="dash_info_box">
            <p className={`info_title ${props.id}`}>{props.nm}</p>
            <p className={`info_value ${props.id === HAMS_DASH_INFO_TYPE[0].id && 'loc_value'}`}
               ref={addrRef}
               onMouseEnter={()=>{
                   const isOver = addrRef.current.scrollWidth>addrRef.current.clientWidth
                   setShowTooltip(isOver)
               }}
               onMouseLeave={()=>{
                   setShowTooltip(false)
               }}
            >
                {props.id === HAMS_DASH_INFO_TYPE[2].id ? <><span>위도:</span>{lat}</> : props.value}
            </p>

            {
                props.id === HAMS_DASH_INFO_TYPE[2].id ?
                    <>
                        <div className="divide_bar"/>
                        <p className="info_value">
                            <span>경도:</span> {lon}
                        </p>
                    </>
                    : ''
            }
            {
                (props.id === HAMS_DASH_INFO_TYPE[0].id && showTooltip) && <div className="tooltip_text">{props.value}</div>
            }
        </div>
    )

}

DashInfoBox.defaultProps = {
    nm: "test1",
    value: "test1"
}

export default DashInfoBox