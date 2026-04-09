import styled from "styled-components"

import AddrSearchBox from "./addrSearchBox";
import AddrSearchList from "./addrSearchList";
import React from "react";


interface Props{
    openLeftTab : boolean
}

const StyledSearchArea = styled.div<Props>`
  width: 280px;
  height: auto;
  position: absolute;
  top: 78px;
  left : ${({openLeftTab})=>openLeftTab ? '465px':'-68px'};
  z-index: 3001;
  transition: left 0.8s ease;
`





const AddrSearchArea = (props: { openLeftTab: boolean; }) => {

    return (
        <StyledSearchArea openLeftTab={props.openLeftTab}>
            <AddrSearchBox/>
            <AddrSearchList/>
        </StyledSearchArea>
    )
}


AddrSearchArea.defaultProps = {
    openLeftTab: true
};



export default AddrSearchArea