import {MutableRefObject, useRef} from "react";
import {useDispatch} from "react-redux";
import {PtzAction, PtzControlCode} from "../../../../data/const/monitorMenuConst";
import {actions} from "../../../saga/action/monitor/monitor"


const PtzControlBox = (props:{isShow:boolean , cctvId : string}) => {

    const dispatch = useDispatch();
    const ptzRef:MutableRefObject<any> = useRef();

    // double click시 전체화면
    const doubleClickEvent = (e: React.MouseEvent<HTMLDivElement>) => {
        let targetPtz = ptzRef.current.previousElementSibling.firstChild

        if(targetPtz.requestFullscreen){
            targetPtz.requestFullscreen();
        }else if(targetPtz.mozRequestFullScreen){
            targetPtz.mozRequestFullScreen();
        }else if(targetPtz.webkitRequestFullscreen){
            targetPtz.webkitRequestFullscreen();
        }else if(targetPtz.msRequestFullscreen){
            targetPtz.msRequestFullscreen();
        }
    };

    const doControl = (code :PtzControlCode , action:PtzAction )=>{
        dispatch(actions.requestDoCctvControl(
            {cctvId : props.cctvId, code : code,action:action}))
    }


    return(
        <div className="ptz_control_area"
             style={{"display" :`${props.isShow? "block":"none"}`}}
             ref={ptzRef}
             onDoubleClick={(e)=>doubleClickEvent(e)}
        >
            <div className="btn_ptz_direct_group" >
                <button type="button" className="btn_ptz_direct ptz_up" onDoubleClick={(e)=>{e.stopPropagation();}}
                    onMouseUp={e=>doControl(PtzControlCode.Up, PtzAction.STOP)}
                    onMouseDown={e=>doControl(PtzControlCode.Up, PtzAction.START)}
                />
                <button type="button" className="btn_ptz_direct ptz_down" onDoubleClick={(e)=>{e.stopPropagation();}}
                        onMouseUp={e=>doControl(PtzControlCode.Down, PtzAction.STOP)}
                        onMouseDown={e=>doControl(PtzControlCode.Down, PtzAction.START)}/>
                <button type="button" className="btn_ptz_direct ptz_left" onDoubleClick={(e)=>{e.stopPropagation();}}
                        onMouseUp={e=>doControl(PtzControlCode.Left, PtzAction.STOP)}
                        onMouseDown={e=>doControl(PtzControlCode.Left, PtzAction.START)}/>
                <button type="button" className="btn_ptz_direct ptz_right" onDoubleClick={(e)=>{e.stopPropagation();}}
                        onMouseUp={e=>doControl(PtzControlCode.Right, PtzAction.STOP)}
                        onMouseDown={e=>doControl(PtzControlCode.Right, PtzAction.START)}/>
            </div>
            <div className="btn_ptz_zoom_group" >
                <button type="button" className="btn_ptz_zoom ptz_plus" onDoubleClick={(e)=>{e.stopPropagation();}}
                        onMouseUp={e=>doControl(PtzControlCode.ZoomIn, PtzAction.STOP)}
                        onMouseDown={e=>doControl(PtzControlCode.ZoomIn, PtzAction.START)}/>
                <button type="button" className="btn_ptz_zoom ptz_minus" onDoubleClick={(e)=>{e.stopPropagation();}}
                        onMouseUp={e=>doControl(PtzControlCode.ZoomOut, PtzAction.STOP)}
                        onMouseDown={e=>doControl(PtzControlCode.ZoomOut, PtzAction.START)}/>
            </div>
        </div>

    )
}
export default PtzControlBox