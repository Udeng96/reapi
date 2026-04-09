import Select, {StylesConfig} from "react-select";
import {useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import {useEffect, useState} from "react";
import {OnChangeValue} from "react-select/dist/declarations/src/types";
import {FireCarInfo,  FireCarResult} from "../../../../../data/interface/event/eventInterface";

import _ from "underscore"

import fireTruckEventIcon
    from "../../../../../public/static/images/dark/page/icon/icon_rightPanel_fireTruck_legend_red.png"
import fireTruckAnotherEventIcon
    from "../../../../../public/static/images/dark/page/icon/icon_rightPanel_fireTruck_legend_blue.png"


const options: MyOptionType[] = [
    {
        value: "전체",
        label: `(<><span style={{}}>영통소방_소방차 001 영통소방_소방차 001</span><img style={{ width: "16px", height: "16px", position: "absolute", top: "7px", left: "8px" }} src={fireTruckBlueIcon} /></>)`
    },
    {value: "미분류", label: "미분류"},
];

type MyOptionType = {
    label: string;
    value: string;
};

type IsMulti = false;


const selectStyle: StylesConfig<MyOptionType, IsMulti> = {
    option: (provided, state) => ({
        ...provided,
        color: '#fafcfc',
        backgroundColor: state.isSelected ? '#452019' : '#11111a',
        paddingTop: 8,
        paddingLeft: 30,
        height: '30px',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
        position: 'relative',
        "&:hover": {
            backgroundColor: state.isSelected ? '#542613' : '#282a38',
        },
    }),

    dropdownIndicator: (provided) => ({
        ...provided,
        color: "#c7c8c9",
        padding: '6px 9px 8px 8px',
        "&:hover": {
            color: "#ff6f26"
        }
    }),

    control: (provided, state) => ({
        ...provided,
        width: '206px',
        height: '35px',
        minHeight: '35px',
        color: '#f7f7f7',
        borderRadius: '5px',
        // backgroundColor: state.isSelected ? '#1c2c73' : '#0f1640',
        backgroundColor: state.isFocused ? '#13151f' : '#262730',
        borderColor: state.isFocused ? '#db5c18' : '#494b57',
        boxShadow: 'none',
        "&:hover": {
            borderColor: state.isFocused ? '#db5c18' : '#5e6170',
            backgroundColor: state.isFocused ? '#13151f' : '#30303b',
        },
    }),

    menu: (provided, state) => ({
        ...provided,
        width: '206px',
        color: '#f7f7f7',
        backgroundColor: '#13151f',
        border: '1px solid #db5c18',
        borderRadius: '5px',
        fontSize: '12px',
        fontFamily: 'NotoSansCJKR',
        margin: '1px 0',
        boxShadowColor: 'none',
    }),

    menuList: (base) => ({
        ...base,

        "::-webkit-scrollbar": {
            width: "3px",
            height: "0px"
        },
        "::-webkit-scrollbar-track": {
            background: "rgba(235, 242, 245,0.1)",
            borderRadius: "1.5px"
        },
        "::-webkit-scrollbar-thumb": {
            background: "#747a8a",
            borderRadius: "1.5px"
        },
        "::-webkit-scrollbar-thumb:hover": {
            background: "#9197a8"
        }
    }),

    indicatorSeparator: (provided) => ({
        ...provided,
        backgroundColor: 'transparent',
    }),

    singleValue: (provided) => ({
        ...provided,
        fontFamily: 'NotoSansCJKR',
        fontSize: '12px',
        color: '#f7f7f7',
        paddingLeft: 20,
        paddingBottom: 2
    }),

    placeholder: (defaultStyles) => {
        return {
            ...defaultStyles,
            fontFamily: 'NotoSansCJKR',
            fontSize: '12px',
            color: '#999ba7',
            paddingLeft: 7,
            paddingBottom: 3
        }
    },
};

const SelectOption = (props: { setVideoInfo: Function }) => {

    const fireCarInfo = useSelector((state: RootState) => state.server.dstrMain.event.fireCarInfoList) as Array<FireCarInfo> | null
    const [options, setOptions] = useState<any[]>([])
    const [selected, setSelected] = useState({value: '', label: '-'})

    useEffect(() => {
        if (fireCarInfo){
            convert2Options(fireCarInfo)
        }

        //현재 실제 출동 차량의 정보가 없기때문에 뒤에 붙이는것은 더미 코드로 실시
    }, [fireCarInfo])


    const convert2Options = (data: Array<FireCarInfo> | null) => {

        if (!data) {
            setOptions([])
            setSelected({value: '', label: '-'})
        } else {
            // let fireCars: any = []
            let liveChannels: any = []
            // if (data.fireCar.length > 0) {
            //     fireCars = data.fireCar.map((item) => {
            //         return {
            //             value: {
            //                 rtsp: item.rtsp,
            //                 mgrNo: item.id
            //             },
            //             nm: item.nm,
            //             label: (<><span style={{}}>{item.nm}</span><img
            //                 style={{width: "16px", height: "16px", position: "absolute", top: "7px", left: "8px"}}
            //                 src={fireTruckEventIcon}/></>)
            //         }
            //     })
            // }
            if (data.length > 0) {
                liveChannels = data.map((item, index) => {
                    return {
                        value: {
                            rtsp: item.videoUrl,
                            mgrNo: item.truckNo
                        },
                        nm: item.truckNo,
                        label: (<><span style={{}}>{item.truckNo}</span><img
                            style={{width: "16px", height: "16px", position: "absolute", top: "7px", left: "8px"}}
                            src={fireTruckAnotherEventIcon}/></>)
                    }
                })
            }
            let options: any[] = []
            // fireCars.forEach((item: any) => {
            //     let findIndex = _.findIndex(options, {
            //         nm: item.nm,
            //     })
            //     if (findIndex == -1) {
            //         options.push(item)
            //     }
            // })
            liveChannels.forEach((item: any) => {
                let findIndex = _.findIndex(options, {
                    nm: item.nm,
                })
                if (findIndex == -1) {
                    options.push(item)
                }
            })
            setOptions(options)
            //최신걸로 영상 출력 요청
            if (options.length > 0) {

                //만약 출동소방차가 없다면 비디 출력을 하지않는다.
                // if (data.fireCar.length !== 0) {
                    props.setVideoInfo(options[0])
                // } else {
                //     props.setVideoInfo({
                //         value: {
                //             rtsp: '',
                //             mgrNo: ''
                //         },
                //         label: '', nm: ''
                //     })
                // }
                setSelected(options[0])
            }

        }


    }


    const handleSelect = (e: OnChangeValue<any, any>) => {
        //영상 표출
        setSelected(e)
        props.setVideoInfo({value: e.value, nm: e.nm})
    };

    return (
        <Select
            className="select_fire_truck"
            options={options}
            value={selected}
            placeholder='소방차 출동영상 선택'
            onChange={handleSelect}
            styles={selectStyle}
        />
    )
}


export default SelectOption