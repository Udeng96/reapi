import { TransCycleOptionType} from "../interface/monitor/monitorInterface";
import {StylesConfig} from "react-select";

export const TransCycleOptions: TransCycleOptionType[] = [
    {value: "10000", label: "10초마다"},
    {value: "60000", label: "1분마다"},
    {value: "300000", label: "5분마다"},
    {value: "600000", label: "10분마다"},
    {value: "1800000", label: "30분마다"}
];

export const PageTransType = {
    NONE: 0,
    PLAY: 1,
    STOP: 2,
    EACH : 3, //개별 CCTV를 삽입하는경우
}

export type PageTransType = typeof PageTransType[keyof typeof PageTransType];


export const LOAD_MORE_WORD = `Load More`

export const PtzControlCode = {
    Up: "Up",
    Down: "Down",
    Left: "Left",
    Right: "Right",
    // LeftUp: "LeftUp",
    // RightUp: "RightUp",
    // LeftDown: "LeftDown",
    // RightDown: "RightDown",
    ZoomIn: "ZoomIn",
    ZoomOut: "ZoomOut",
}
export type PtzControlCode = typeof PtzControlCode[keyof typeof PtzControlCode]

export const PtzAction = {
    START : "START",
    STOP : "STOP"
}
export type PtzAction = typeof PtzAction[keyof typeof PtzAction]

export const MenuSelectStyle: StylesConfig = {
    option: (provided, state) => ({
        ...provided,
        color: state.isSelected ? "#ff6f26" : '#fafcfc',
        backgroundColor: state.isSelected ? '#452019' : '#11111a',
        paddingTop: 9,
        paddingLeft: 17,
        height: '31px',
        "&:hover": {
            backgroundColor: state.isSelected ? '#452019' : '#282a38',
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
        width: '202px',
        height: '38px',
        minHeight: '38px',
        color: '#f7f7f7',
        borderRadius: '6px',
        // backgroundColor: state.isSelected ? '#1c2c73' : '#0f1640',
        backgroundColor: state.isFocused ? '#11111a' : '#22232e',
        borderColor: state.isFocused ? '#db5c18' : '#3d3f4d',
        boxShadow: 'none',
        "&:hover": {
            borderColor: state.isFocused ? '#db5c18' : '#4f5163',
            backgroundColor: state.isFocused ? '#11111a' : '#22232e',
        }
    }),
    menu: (provided, state) => ({
        ...provided,
        color: '#f7f7f7',
        backgroundColor: '#11111a',
        border: '1px solid #db5c18',
        borderRadius: '6px',
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
        paddingLeft: 7,
        paddingBottom: 2
    }),

    placeholder: (defaultStyles) => {
        return {
            ...defaultStyles,
            fontFamily: 'NotoSansCJKR',
            fontSize: '12px',
            color: '#f7f7f7',
            paddingLeft: 7,
            paddingBottom: 4
        }
    }
};


