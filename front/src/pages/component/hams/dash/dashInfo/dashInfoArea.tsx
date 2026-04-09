import DashInfoBox from "./dashInfoBox";
import {HAMS_DASH_INFO_TYPE} from "../../../../../data/const/hamsConst";
import {HamsGisFacInfo} from "../../../../../data/interface/hams/hamsInterface";

const DashInfoArea = (props:{activeItem:HamsGisFacInfo|null}) => {

    return(
        <>
            <div className="dash_info_block">
                <DashInfoBox id={HAMS_DASH_INFO_TYPE[0].id}
                             nm={HAMS_DASH_INFO_TYPE[0].nm}
                             value={props.activeItem!==null? props.activeItem.addr!! : ""}
                />
                <DashInfoBox id={HAMS_DASH_INFO_TYPE[1].id}
                             nm={HAMS_DASH_INFO_TYPE[1].nm}
                             value={props.activeItem!==null? props.activeItem.groupNm!! : ""}
                />
            </div>
            <div className="dash_info_block">
                <DashInfoBox id={HAMS_DASH_INFO_TYPE[2].id}
                             nm={HAMS_DASH_INFO_TYPE[2].nm}
                             value={props.activeItem!==null?`${props.activeItem!!.lat},${props.activeItem!!.lng}` : "0,0"}
                />
            </div>
        </>
    )

}

DashInfoArea.defaultProps={
    activeItem : null
}

export default DashInfoArea