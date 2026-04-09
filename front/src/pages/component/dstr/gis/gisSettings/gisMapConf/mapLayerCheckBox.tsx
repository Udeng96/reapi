import React, {useEffect, useState} from "react";
import {MapTypeProps} from "../../../../../../data/interface/map/mapInterface";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../../store/rootStore";
import {addCustomLayer, removeCustomLayer} from "../../../../../store/view/dstr/mapStore";
import _ from "underscore"

const MapLayerCheckBox:React.FC<MapTypeProps> = ({type ,mapNm,mapTypeNm}) => {

    const dispatch = useDispatch();
    const [isChecked, setCheck] = useState(false);

    const customLayerList = useSelector((state:RootState)=>state.view.map.customLayerList)

    useEffect(() => {

        if (isChecked){
            dispatch(addCustomLayer({type:type , mapNm : mapNm, mapTypeNm: mapTypeNm }))
        }else {
            dispatch(removeCustomLayer({type:type , mapNm : mapNm, mapTypeNm: mapTypeNm }))
        }

    }, [isChecked]);

    useEffect(() => {

        let findIndex = _.findIndex(customLayerList,{
            type : type,
            mapNm : mapNm
        })
        if (findIndex >-1){
            setCheck(true)
        }else {
            setCheck(false)
        }

    }, [customLayerList]);



    return(
        <>
            <div className="check_box">
                <label className="container">
                    <input type="checkbox" checked={isChecked} onChange={()=>setCheck(!isChecked)}/>
                    <span className="checkmark"/>
                    <div className="check_name">{mapTypeNm}</div>
                </label>
            </div>
        </>
    )
}

export default MapLayerCheckBox