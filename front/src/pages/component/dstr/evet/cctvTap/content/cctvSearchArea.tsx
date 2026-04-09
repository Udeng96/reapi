import React, { useEffect, useState } from "react";
import Select, { StylesConfig } from "react-select";
import moment from "moment";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../../../store/rootStore";
import { ClfyInterface } from "../../../../../../data/interface/common/CommonInterface";
import { OnChangeValue, SingleValue } from "react-select/dist/declarations/src/types";
import { updateSelectClfy, updateWord } from "../../../../../store/view/dstr/dstrViewCctvStore";
import * as actions from "../../../../../saga/action/dstr/cctv/cctvActions"

// const options: MyOptionType[] = [
//     { value: "전체", label: "전체" },
//     { value: "미분류", label: "미분류" },
//     { value: "산사태/붕괴", label: "산사태/붕괴" },
//     { value: "해안/선착장", label: "해안/선착장" },
//     { value: "방범", label: "방범" },
//     { value: "경사로", label: "경사로" }
// ];
//
type MyOptionType = {
    label: string;
    value: string;
};

type IsMulti = false;

const selectStyle: StylesConfig = {
    option: (provided, state) => ({
        ...provided,
        color: '#fafcfc',
        backgroundColor: state.isSelected ? '#452019' : '#11111a',
        paddingTop: 8,
        paddingLeft: 17,
        height: '30px',
        "&:hover": {
            backgroundColor: state.isSelected ? '#542613' : '#282a38',
        }
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
        width: '382px',
        height: '38px',
        minHeight: '38px',
        color: '#fafcfc',
        borderRadius: '6px',
        // backgroundColor: state.isSelected ? '#1c2c73' : '#0f1640',
        backgroundColor: state.isFocused ? '#11111a' : '#22232e',
        borderColor: state.isFocused ? '#db5c18' : '#3d3f4d',
        boxShadow: 'none',
        "&:hover": {
            borderColor: state.isFocused ? '#db5c18' : '#4f5163',
            backgroundColor: state.isFocused ? '#11111a' : '#2a2b38',
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
            fontSize: '13px',
            color: 'rgba(255,255,255,0.4)',
            paddingLeft: 7,
            paddingBottom: 4
        }
    }
};

const CctvSearchArea = (props: { setCurrentPage: Function }) => {

    const firtOption = { value: "", label: "전체" }
    const dispatch = useDispatch()
    const [date, setDate] = useState<moment.Moment>(moment())
    const [options, setOptions] = useState<MyOptionType[]>([firtOption])
    const clfyList = useSelector((state: RootState) => state.server.dstrMain.cctv.clfyList)

    const [selectClfy, setSelectClfy] = useState<MyOptionType>(firtOption)
    const [word, setWord] = useState('')


    const selectHandler = (option: MyOptionType) => {
        setSelectClfy(option)
    }

    useEffect(() => {

        if (clfyList != null) {
            const newClfy = (clfyList!! as ClfyInterface[]).map((item: ClfyInterface) => {
                return { label: item.nm, value: item.id }
            })
            newClfy.unshift(firtOption)
            setOptions(newClfy)
        }


    }, [clfyList])

    //TODO 이렇게 변경하고 currentPage가 1이 되었다는걸 알려줘야한다
    const doSearchCctv = () => {
        setDate(moment())
        dispatch(updateSelectClfy({ id: selectClfy.value, nm: selectClfy.label } as ClfyInterface))
        dispatch(updateWord(word))

        props.setCurrentPage(1)

        dispatch(actions.actions.fetchList({
            page: 1,
            rows: 11,
            type: selectClfy.value,
            cctvNm: word
        }))
    }

    return (
        <>
            <div className="search_setting_area">
                <h2 className="dash_sub_title cctv_search_title">CCTV 검색 조건 설정</h2>
                <div className="select_block">
                    <p className="select_title"><span>&middot;</span>설치 목적</p>
                    <Select options={options} styles={selectStyle} defaultValue={firtOption}
                        onChange={(e: SingleValue<any>) => selectHandler(e)}
                    />
                </div>
                <div className="select_block">
                    <p className="select_title"><span>&middot;</span>명칭 입력</p>
                    <input type="text" className="search_input" placeholder="검색할 명칭을 입력해 주세요"
                           onKeyPress={e=>e.key==='Enter'&&doSearchCctv()}
                        onKeyDown={(e)=>{
                            if(e.code === 'Enter'){
                                doSearchCctv()
                            }
                        }}
                        onChange={e => setWord(e.target.value)}
                    />
                </div>
                <button type="button" className="btn_search"
                    onClick={doSearchCctv}
                >검색<span>(업데이트 일시 : {date.format('yyyy - MM - DD HH:mm:ss')})</span>
                </button>
            </div>
        </>
    )
}

export default CctvSearchArea