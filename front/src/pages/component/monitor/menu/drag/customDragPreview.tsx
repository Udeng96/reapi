import {DragLayerMonitorProps} from "@minoru/react-dnd-treeview";

const CustomDragPreview = (props:{monitorProps:DragLayerMonitorProps<any>}) => {

    const item = props.monitorProps.item;

    return(
        <div>
            <div style={{"color":"white"}}>
                {item.text}
            </div>
        </div>
    )

}
export default CustomDragPreview