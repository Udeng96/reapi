import styled from 'styled-components';
import GisCctvPopupHeader from '../../molecules/GisCctvPopupHeader/GisCctvPopupHeader';
import { useState } from 'react';
import dynamic from 'next/dynamic';
const GisCctvPopupBody = dynamic(
  () => import('../../molecules/GisCctvPopupBody/GisCctvPopupBody'),
  {
    ssr: false,
  }
);


// width: ${({ isExpand }) => {
//   isExpand ? '684px' : '344px';
// }};
// height: ${({ isExpand }) => {
//   isExpand ? '412px' : '222px';
// }};
const StyledGisCctvPopup = styled.div`
  width: 344px;
  height: 222px;
  border: solid 2px #009876;
  background-color: #1a1d1f;
  position: absolute;
  margin-top: 9px;
  // margin-left: 54px;
  margin-left: 29px;
`;



const GisCctvPopup = ({item,closePopup}) => {
  const [isExpand, toggleExpand] = useState(false);

  function toggleSize(value) {
    toggleExpand(value);
  }

  return (
    <StyledGisCctvPopup isExpand={isExpand} style={{width: isExpand? '684px' : '344px', height: isExpand? '412px' : '222px'}}>
      <GisCctvPopupHeader
        isExpand={isExpand}
        id={item.facId}
        closePopup={closePopup}
      >
        {item.facNm}
      </GisCctvPopupHeader>
      <GisCctvPopupBody
        isExpand={isExpand}
        id={item.facId}
        toggleExpand={toggleSize}
      />
    </StyledGisCctvPopup>
  );
};

export default GisCctvPopup;
