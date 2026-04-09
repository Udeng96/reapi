import styled from "styled-components";

import stateLockIcon from "../../../../../public/static/images/dark/page/icon/hams/lock_lock_apng.png"
import stateUnLockIcon from "../../../../../public/static/images/dark/page/icon/hams/lock_unlock_apng.png"

import stateNoDataIcon from "../../../../../public/static/images/dark/page/icon/hams/nodata.png"

import stateOpenDoorIcon from "../../../../../public/static/images/dark/page/icon/hams/frontDoor_open_apng.png"
import stateCloseDoorIcon from "../../../../../public/static/images/dark/page/icon/hams/frontDoor_close_apng.png"

import stateConnectedIcon from "../../../../../public/static/images/dark/page/icon/hams/connected_apng.png"
import stateDisConnectedIcon from "../../../../../public/static/images/dark/page/icon/hams/unconnected.png"

import {useEffect, useState} from "react";
import {
    HAMS_DOOR_LOCK_TYPE,
    HAMS_DOOR_STATE_TYPE,
    HAMS_REAL_TIME_BOX_TYPE,
    HAMS_REAL_TIME_VALUE_COLOR
} from "../../../../../data/const/hamsConst";
import {useDispatch, useSelector} from "react-redux";
import {updateChartPopupOpen, updateLockPopupOpen} from "../../../../store/view/hams/hamsViewStore";
import {RootState} from "../../../../store/rootStore";
import {WsDoeInfo} from "../../../../../data/interface/hams/hamsInterface";

const StyledRealTimeDataBox = styled.div<{ isSep: boolean }>`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: ${({isSep}) => isSep ? 'horizontal;' : 'vertical;'}
  -webkit-box-direction: normal;
  -ms-flex-direction: ${({isSep}) => isSep ? 'row;' : 'column;'}
  flex-direction: ${({isSep}) => isSep ? 'row;' : 'column;'}
  width: 273px;
  height: 153px;
  border-radius: 12px;
  -webkit-box-shadow: 0px 8px 16px 0 rgba(0, 0, 0, 0.08);
  box-shadow: 0px 8px 16px 0 rgba(0, 0, 0, 0.08);
  border: solid 1px #1f202e;
  background-image: linear-gradient(110deg, #1f202e 3%, #1d1e2b 46%, #1b1c28 97%);
  margin-bottom: 8px;
  padding: ${({isSep}) => isSep ? '18px 27px 0 18px;' : '18px 0 0 18px;'}
  position: relative;

  -webkit-box-pack: ${({isSep}) => isSep ? 'justify;' : ''}
  -ms-flex-pack: ${({isSep}) => isSep ? 'justify;' : ''}
  justify-content: ${({isSep}) => isSep ? 'space-between;' : ''}
`

const StyledRealTimeIcon = styled.div<{ icon: string }>`
  width: 110px;
  height: 110px;
  position: absolute;
  top: 24px;
  right: 8px;
  background: ${({icon}) => `url(${icon})`}
`

const StyledRealTimeTitle = styled.p<{ titleWidth: string }>`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  height: 28px;
  border-radius: 14px;
  background-image: -webkit-gradient(linear, left top, right top, from(#2d2e40), color-stop(46%, #252635), to(#1c1d29));
  background-image: linear-gradient(to right, #2d2e40, #252635 46%, #1c1d29);
  padding-left: 12.3px;
  font-family: "NotoSansCJKR";
  font-size: 12px;
  color: #a5a7b5;
  margin-bottom: 14.5px;
  z-index: 1;
  width: ${({titleWidth}) => titleWidth ? titleWidth : ''}

`

const StyledRealTimeValue = styled.p<{ valueColor: string }>`
  font-family: "NotoSansCJKR";
  font-size: 16px;
  margin-left: 13.3px;
  z-index: 1;
  color: ${({valueColor}) => valueColor ? valueColor : ''}
`

const StyledRealTimeSeperatedValue = styled.p<{ isTemp: boolean, isRed: boolean }>`
  font-family: "NotoSansCJKM";
  font-size: 24px;
  letter-spacing: -0.6px;
  margin-left: 13.3px;
  color: ${({isTemp, isRed}) => isTemp ? isRed ? '#f03a46;' : '#1a8cff;' : '#5d73f0;'}
`

const StyledRealTimeSeperatedValueSpan = styled.span`
  font-size: 16px;
`

const StyledRealTimeBtnLockGroup = styled.div`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  width: 158px;
  height: 32px;
  margin-top: 18.5px;
  z-index: 1;
`

const StyledRealTimeBtnLock = styled.button`
  width: 76px;
  height: 32px;
  border-radius: 16px;
  border: solid 1px #4f5163;
  background-color: #21232e;
  font-family: "NotoSansCJKM";
  text-shadow: 0 0 4px rgba(0, 0, 0, 0.16);
  font-size: 12px;
  color: #e1e2e3;

  &:hover {
    border-color: #4f5163;
    background-color: #2b2d3a;
    color: #fff;
  }

  &:active {
    border-color: #cf5717;
    background-color: #1a1b26;
    color: #ff6f26;
  }

  &:disabled {
    border-color: #323442;
    background-color: #1d1e2a;
    color: #555869;
  }

`

const StyledRealTimeBtnChart = styled.button`
  width: 117px;
  height: 18px;
  font-family: "NotoSansCJKL";
  text-shadow: 0 0 4px rgba(0, 0, 0, 0.16);
  font-size: 12px;
  letter-spacing: 0.3px;
  text-align: left;
  color: #a5a7b5;
  text-decoration: underline;
  position: relative;
  margin: 25px 0 0 14px;

  &::after {
    content: "→";
    display: block;
    font-family: "NotoSansCJKL";
    font-size: 12px;
    color: #a5a7b5;
    position: absolute;
    top: 1px;
    right: 0;

  }

  &:hover {
    color: #fff;

    &::after {
      color: #fff;
    }
  }

  &:active {
    color: #ff6f26;

    &::after {
      color: #ff6f26;
    }
  }

  &:disabled {
    color: #555869;

    &::after {
      color: #555869;
    }
  }
`

const StyledRealTimeConnectText = styled.p<{ isConnect: boolean | null }>`
  font-family: "NotoSansCJKL";
  font-size: 10px;
  letter-spacing: 0.5px;
  opacity: 0.33;
  margin: 25px 0 0 13.3px;
  color: ${({isConnect}) => (isConnect !== null && isConnect === true) ? ' #42e3d3;' : '#ba3ce8;'}
`

const StyledRealTimeSeperatedArea = styled.div<{ isTemp: boolean }>`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  font-family: NotoSansCJKM;
  font-size: 24px;
  letter-spacing: -0.6px;
  color: ${({isTemp}) => isTemp ? "#f03a46;" : "#5d73f0;"}
  margin-left: 13.3px;

`

const StyledRealTimeTitleSpan = styled.span`
  font-family: "NotoSansL";
  font-size: 10px;
  letter-spacing: 0.5px;
  text-align: left;
  color: #777987;
  margin-left: 4px;
`

const StyledRealTimeSeperatedUnitText = styled.span<{ isTemp: boolean }>`
  font-family: "NotoSansCJKL";
  font-size: 14px;
  letter-spacing: -0.35px;
  color: #777987;
  margin-left: 6px;
`

const StyledRealTimeSeperatedLimitText = styled.p`
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  width: 109px;
  height: 21px;
  border-radius: 10.5px;
  background-color: #191a24;
  font-family: "NotoSansCJKL";
  text-shadow: 0 0 4px rgba(0, 0, 0, 0.16);
  font-size: 12px;
  letter-spacing: 0.3px;
  color: #777987;
  margin: 15.5px 0 0 6px;
`

const StyledRealTimeSeperatedLimitSpan = styled.span`
  color: #a62929;
  margin: 0 2px 0 6px;
  padding-top: 1px;
`

const RealtimeDataBox = (props: { type: string, isSep: boolean }) => {

    let activeItem = useSelector((state: RootState) => state.server.hams.activeHamsInfo)
    // let activeItem = useSelector((state: RootState) => state.view.hams.activeHamsInfo)
    let lockRequestLockUnLockBtn = useSelector((state: RootState) => state.view.hams.lockRequestLockUnLockBtn)
    let wsDoeInfo : WsDoeInfo | null = useSelector((state: RootState) => state.server.hams.wsDoeInfo)
    // let wsTestDoeInfo : WsDoeInfo | null = useSelector((state: RootState) => state.server.hams.wsDoeInfo)

    const dispatch = useDispatch()

    // DOOR STATUS
    const [icon, setIcon] = useState<string>("")
    const [titleStyle, setTitleStyle] = useState<string>("")
    const [title, setTitle] = useState<string>("")
    const [color, setColor] = useState<string>("")
    const [value, setValue] = useState<string>("")
    const [isLock, setIsLock] = useState<boolean | null>(null)
    const [isOpen, setIsOpen] = useState<boolean | null>(null)
    const [isConnect, setIsConnect] = useState<boolean | null>(false)


    // DOOR STATUS
    useEffect(() => {

        // LOCK
        if (HAMS_REAL_TIME_BOX_TYPE[0].typeId === props.type) {
            setTitleStyle(HAMS_REAL_TIME_BOX_TYPE[0].titleStyle)
            setTitle(HAMS_REAL_TIME_BOX_TYPE[0].typeNm)

            let icon = stateNoDataIcon;
            let color = HAMS_REAL_TIME_VALUE_COLOR.tGray;
            let value = HAMS_REAL_TIME_BOX_TYPE[0].value.NONE!!
            let isLock = null;


            if (activeItem && activeItem.status!!.frontDoorLock === HAMS_DOOR_LOCK_TYPE.LOCK) {
                icon    =   stateLockIcon
                color   =   HAMS_REAL_TIME_VALUE_COLOR.tSky
                value   =   HAMS_REAL_TIME_BOX_TYPE[0].value.LOCK!!
                isLock  =   true
            } else if (activeItem && activeItem.status!!.frontDoorLock === HAMS_DOOR_LOCK_TYPE.UNLOCK) {
                icon    = stateUnLockIcon
                color   = HAMS_REAL_TIME_VALUE_COLOR.tRed
                value   = HAMS_REAL_TIME_BOX_TYPE[0].value.UNLOCK!!
                isLock  = false
            }

            if (wsDoeInfo != null){
                if (activeItem && activeItem.hamsId =='HAMS_'+parseInt(wsDoeInfo?.EQ_ID.substring(4))){//장비 id가 동일한 경우에만 실행한다.
                    if (wsDoeInfo?.FRONT_DOOR_LOCK_S  === '2'){
                        icon    = stateUnLockIcon
                        color   = HAMS_REAL_TIME_VALUE_COLOR.tRed
                        value   = HAMS_REAL_TIME_BOX_TYPE[0].value.UNLOCK!!
                        isLock  = false
                    }else if (wsDoeInfo?.FRONT_DOOR_LOCK_S  === '3'){
                        icon    =   stateLockIcon
                        color   =   HAMS_REAL_TIME_VALUE_COLOR.tSky
                        value   =   HAMS_REAL_TIME_BOX_TYPE[0].value.LOCK!!
                        isLock  =   true
                    }else {
                        icon = stateNoDataIcon;
                        color = HAMS_REAL_TIME_VALUE_COLOR.tGray;
                        value = HAMS_REAL_TIME_BOX_TYPE[0].value.NONE!!
                        isLock = null;
                    }
                }
            }
            setIcon(icon)
            setColor(color)
            setValue(value)
            setIsLock(isLock)

            // STATE
        } else if (HAMS_REAL_TIME_BOX_TYPE[1].typeId === props.type) {
            setTitleStyle(HAMS_REAL_TIME_BOX_TYPE[1].titleStyle)
            setTitle(HAMS_REAL_TIME_BOX_TYPE[1].typeNm)

            let icon    = stateNoDataIcon;
            let color   = HAMS_REAL_TIME_VALUE_COLOR.tGray;
            let value   = HAMS_REAL_TIME_BOX_TYPE[1].value.NONE!!
            let isOpen  = null;


            if (activeItem && activeItem.status!!.frontDoor === HAMS_DOOR_STATE_TYPE.OPEN) {
               icon    = stateOpenDoorIcon
               color   = HAMS_REAL_TIME_VALUE_COLOR.tRed
               value   = HAMS_REAL_TIME_BOX_TYPE[1].value.OPEN!!
               isOpen  = true
            } else if (activeItem && activeItem.status!!.frontDoor === HAMS_DOOR_STATE_TYPE.CLOSE) {
                icon    =stateCloseDoorIcon
                color   =HAMS_REAL_TIME_VALUE_COLOR.tSky
                value   =HAMS_REAL_TIME_BOX_TYPE[1].value.CLOSE!!
                // isOpen  =
            } else {
                icon    =stateNoDataIcon
                color   =HAMS_REAL_TIME_VALUE_COLOR.tGray
                value   =HAMS_REAL_TIME_BOX_TYPE[1].value.NONE!!
            }

            if (wsDoeInfo != null){
                if (activeItem && activeItem.hamsId =='HAMS_'+parseInt(wsDoeInfo?.EQ_ID.substring(4))){//장비 id가 동일한 경우에만 실행한다.
                    if (wsDoeInfo?.FRONT_DOOR_S  === '2'){
                        icon    = stateOpenDoorIcon
                        color   = HAMS_REAL_TIME_VALUE_COLOR.tRed
                        value   = HAMS_REAL_TIME_BOX_TYPE[1].value.OPEN!!
                        isOpen  = true
                    }else if (wsDoeInfo?.FRONT_DOOR_S  === '3'){
                        icon    =stateCloseDoorIcon
                        color   =HAMS_REAL_TIME_VALUE_COLOR.tSky
                        value   =HAMS_REAL_TIME_BOX_TYPE[1].value.CLOSE!!
                    }else {
                        icon    =stateNoDataIcon
                        color   =HAMS_REAL_TIME_VALUE_COLOR.tGray
                        value   =HAMS_REAL_TIME_BOX_TYPE[1].value.NONE!!
                    }
                }
            }
            setIcon(icon)
            setColor(color)
            setValue(value)
            setIsOpen(isOpen)

            // CONNECT
        } else {
            setTitleStyle(HAMS_REAL_TIME_BOX_TYPE[2].titleStyle)
            setTitle(HAMS_REAL_TIME_BOX_TYPE[2].typeNm)

            if (activeItem && activeItem.status!!.isConnect === true) {
                setIcon(stateConnectedIcon)
                setIsConnect(true)
                setColor(HAMS_REAL_TIME_VALUE_COLOR.tMint)
                setValue(HAMS_REAL_TIME_BOX_TYPE[2].value.CONN!!)

            } else if (activeItem && activeItem.status!!.isConnect === false) {
                setIcon(stateDisConnectedIcon)
                setIsConnect(false)
                setColor(HAMS_REAL_TIME_VALUE_COLOR.tPurple)
                setValue(HAMS_REAL_TIME_BOX_TYPE[2].value.DISCONN!!)

            } else {
                setIcon(stateNoDataIcon)
                setIsConnect(null)
                setColor(HAMS_REAL_TIME_VALUE_COLOR.tPurple)
                setValue(HAMS_REAL_TIME_BOX_TYPE[2].value.NONE!!)
            }

        }
    }, [activeItem, wsDoeInfo])


    // DOOR TEMP & HUMI
    const [temp, setTemp] = useState<string[]>([])
    const [humid, setHumid] = useState<string[]>([])


    // DOOR TEMP & HUMI
    useEffect(() => {
        let newTemp: string[] = [];
        let newHumi: string [] = [];

        if (activeItem !== null) {
            if (Number.isInteger(activeItem.status!!.temp)) {
                newTemp.push(activeItem.status!!.temp ? String(activeItem.status!!.temp) : "0")
                newTemp.push("0")
                setTemp(newTemp)
            } else {
                newTemp = String(activeItem.status!!.temp).split(".")
                setTemp(newTemp)
            }

            if (Number.isInteger(activeItem.status!!.humi)) {
                newHumi.push(activeItem.status!!.humi ? String(activeItem.status!!.humi) : "0")
                newHumi.push("0")
                setHumid(newHumi)
            } else {
                newHumi = String(activeItem.status!!.humi).split(".")
                setHumid(newHumi)
            }

        }

    }, [activeItem])


    const openLockPopup = () => {
        dispatch(updateLockPopupOpen(true))
    }

    const openChartPopup = () => {
        dispatch(updateChartPopupOpen(true))
    }

    return (
        <>
            {
                props.isSep ?

                    <StyledRealTimeDataBox isSep={true}>
                        <StyledRealTimeSeperatedArea isTemp={true}>
                            <StyledRealTimeTitle titleWidth={"113px"}>온도
                                <StyledRealTimeTitleSpan>Temperature</StyledRealTimeTitleSpan>
                            </StyledRealTimeTitle>
                            <StyledRealTimeSeperatedValue
                                isRed={!!(activeItem && activeItem.status!!.temp > 30)} isTemp={true}>
                                {temp.length !== 0 ? temp[0] : "noData"}
                                <StyledRealTimeSeperatedValueSpan>{temp.length !== 0 ? "." + temp[1] : ""}</StyledRealTimeSeperatedValueSpan>
                                <StyledRealTimeSeperatedUnitText
                                    isTemp={true}>{temp.length !== 0 ? "℃" : ""}</StyledRealTimeSeperatedUnitText>
                            </StyledRealTimeSeperatedValue>
                            <StyledRealTimeSeperatedLimitText>상한 온도
                                <StyledRealTimeSeperatedLimitSpan>40.0</StyledRealTimeSeperatedLimitSpan>
                                ℃
                            </StyledRealTimeSeperatedLimitText>
                        </StyledRealTimeSeperatedArea>

                        <StyledRealTimeSeperatedArea isTemp={false}>
                            <StyledRealTimeTitle titleWidth={"96px"}>습도
                                <StyledRealTimeTitleSpan>Humidity</StyledRealTimeTitleSpan>
                            </StyledRealTimeTitle>
                            <StyledRealTimeSeperatedValue isRed={false} isTemp={false}>
                                {humid.length !== 0 ? humid[0] : "noData"}
                                <StyledRealTimeSeperatedValueSpan>{humid.length !== 0 ? "." + humid[1] : ""}</StyledRealTimeSeperatedValueSpan>
                                <StyledRealTimeSeperatedUnitText
                                    isTemp={false}>{humid.length !== 0 ? "%" : ""}</StyledRealTimeSeperatedUnitText>
                            </StyledRealTimeSeperatedValue>
                        </StyledRealTimeSeperatedArea>


                    </StyledRealTimeDataBox>

                    :

                    <StyledRealTimeDataBox isSep={false}>
                        <StyledRealTimeIcon icon={icon}/>
                        <StyledRealTimeTitle titleWidth={titleStyle}>{title}</StyledRealTimeTitle>
                        <StyledRealTimeValue valueColor={color}>{value}</StyledRealTimeValue>
                        {
                            (props.type === HAMS_REAL_TIME_BOX_TYPE[0].typeId) ?
                                <StyledRealTimeBtnLockGroup>
                                    <StyledRealTimeBtnLock disabled={(!!isLock || isLock === null) || lockRequestLockUnLockBtn}
                                                           onClick={event => openLockPopup()}>잠금</StyledRealTimeBtnLock>
                                    <StyledRealTimeBtnLock disabled={(!isLock || isLock === null) || lockRequestLockUnLockBtn}
                                                           onClick={event => openLockPopup()}>잠금해제</StyledRealTimeBtnLock>
                                </StyledRealTimeBtnLockGroup>
                                : (props.type === HAMS_REAL_TIME_BOX_TYPE[1].typeId) ?
                                    <StyledRealTimeBtnChart
                                                            onClick={event => openChartPopup()}>앞문 개폐 이력 보기
                                    </StyledRealTimeBtnChart>
                                    : <StyledRealTimeConnectText
                                        isConnect={isConnect}>{isConnect ? '_Connected' : '_Unconnected'}</StyledRealTimeConnectText>

                        }

                    </StyledRealTimeDataBox>

            }
        </>
    )

}

RealtimeDataBox.defaultProps = {
    value: null,
    temp: null,
    humid: null
}

export default RealtimeDataBox