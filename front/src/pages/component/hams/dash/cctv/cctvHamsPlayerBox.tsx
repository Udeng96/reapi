import styled from "styled-components";

import smallReloadBtn from "../../../../../public/static/images/dark/page/btn/hams/btn_reload_small_normal.png"
import smallReloadHoverBtn from "../../../../../public/static/images/dark/page/btn/hams/btn_reload_small_hover.png"
import smallReloadActiveBtn from "../../../../../public/static/images/dark/page/btn/hams/btn_reload_small_click.png"

import bigReloadBtn from "../../../../../public/static/images/dark/page/btn/hams/btn_reload_big_normal.png"
import bigReloadHoverBtn from "../../../../../public/static/images/dark/page/btn/hams/btn_reload_big_hover.png"
import bigReloadActiveBtn from "../../../../../public/static/images/dark/page/btn/hams/btn_reload_big_click.png"
import CctvHamsPlayer from "./cctvHamsPlayer";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../../../store/rootStore";
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation } from 'swiper/modules';
import {updateActiveHamsCctvInfo} from "../../../../store/view/hams/hamsViewStore";



const CctvHamsPlayerBox = (props:{isPopup:boolean}) => {

    const dispatch = useDispatch();
    const activeCctvList = useSelector((state:RootState)=>state.server.hams.hamsCctvList);
    const activeHamsCctvInfo = useSelector((state:RootState)=>state.view.hams.activePlayHamsCctv);


    const handleSwiper = (e:any) => {
        // setSwiperIndex(e.activeIndex);
        dispatch(updateActiveHamsCctvInfo(activeCctvList[e.activeIndex]))
    }

    return (
        <StyledCctvArea isPopup={props.isPopup}>
            {

                props.isPopup ?
                    activeHamsCctvInfo ?
                    <StyledCctvIn>
                        <CctvHamsPlayer isPopup={props.isPopup} item={activeHamsCctvInfo}/>
                    </StyledCctvIn>
                        :
                        <StyledCctvLoadErrorArea isPopup={true}>
                            <StyledErrorMessage isPopup={true}>영상이 존재하지 않습니다.</StyledErrorMessage>
                        </StyledCctvLoadErrorArea>
                    :
                activeCctvList.length === 1 ?
                    <StyledCctvIn>
                        <CctvHamsPlayer isPopup={false} item={activeCctvList[0]}></CctvHamsPlayer>
                    </StyledCctvIn>
                    :
                    activeCctvList.length > 1 ?
                        <Swiper onSlideChange={(swiper: any, event: any) => handleSwiper(swiper)} navigation={true} style={{height:'100%', width:'100%'}} modules={[Navigation]} slidesPerView={1} className="mySwiper">
                            {
                                activeCctvList.map((item)=>(
                                    <SwiperSlide>
                                            <CctvHamsPlayer isPopup={false} key={item.cctvId} item={item}/>
                                    </SwiperSlide>

                                ))
                            }
                        </Swiper>
                        :
                        <StyledCctvLoadErrorArea isPopup={false}>
                            <StyledErrorMessage isPopup={false}>영상이 존재하지 않습니다.</StyledErrorMessage>
                        </StyledCctvLoadErrorArea>

            }

        </StyledCctvArea>
    )

}

CctvHamsPlayerBox.defaultProps = {
    isPopup: false,
    item: null
}

export default CctvHamsPlayerBox


const StyledCctvArea = styled.div<{ isPopup: boolean }>`
    width: ${({isPopup}) => isPopup ? '1600px;' : '554px;'}
    height: ${({isPopup}) => isPopup ? '900px;' : '312px;'}
    border: ${({isPopup}) => isPopup ? 'solid 2px #2d2e40;' : 'solid 1px #242533;'}
    background-color: #101117;
    position: relative;
`

const StyledCctvLoadErrorArea = styled.div<{ isPopup: boolean }>`
    width: 100%;
    height: 100%;
    line-height: 312px;
    //padding-top: ${({isPopup}) => isPopup ? '477px;' : '180px;'}
    position: absolute;
    top: 0;
    left: 0;
`

const StyledReloadBtn = styled.div<{ isPopup: boolean }>`
    width: ${({isPopup}) => isPopup ? '48px;' : '32px;'}
    height: ${({isPopup}) => isPopup ? '48px;' : '32px;'}
    background: ${({isPopup}) => isPopup ? `url(${bigReloadBtn})` : `url(${smallReloadBtn})`} no-repeat;
    position: absolute;
    top: ${({isPopup}) => isPopup ? '401px;' : '116px;'}
    left: 50%;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);

    &:hover {
        background: ${({isPopup}) => isPopup ? `url(${bigReloadHoverBtn})` : `url(${smallReloadHoverBtn})`}
    }

    &:active {
        background: ${({isPopup}) => isPopup ? `url(${bigReloadActiveBtn})` : `url(${smallReloadActiveBtn})`}
    }

    &::after {

        content: "Reload";
        display: block;
        font-family: "NotoSansCJKL";
        font-size: ${({isPopup}) => isPopup ? '12px;' : '10px;'}
        letter-spacing: ${({isPopup}) => isPopup ? '0.6px;' : '0.5px;'}
        color: #a5a7b5;
        position: absolute;
        top: ${({isPopup}) => isPopup ? '49px;' : ' 35px;'}
        left: ${({isPopup}) => isPopup ? '3px;' : ' -1px;'}

    }
`
const StyledErrorMessage = styled.div<{ isPopup: boolean }>`
    font-family: "NotoSansCJKL";
    font-size: ${({isPopup}) => isPopup ? '16px;' : '13px;'}
    text-align: center;
    color: #a5a7b5;
`

const StyledCctvIn = styled.div`
    width: 100%;
    height: 100%;
    overflow:hidden;
`