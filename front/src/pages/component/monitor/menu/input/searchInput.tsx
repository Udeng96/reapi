import styled from "styled-components";

const StyledSearchInput = styled.input`
  width: 202px;
  height: 38px;
  border-radius: 6px;
  border: solid 1px #30323d;
  background-color: #0e0e17;
  font-family: "NotoSansCJKR";
  font-size: 12px;
  letter-spacing: 0.3px;
  text-align: left;
  color: #fff;
  padding: 0 35px 0 15px;
  -webkit-transition: all .25s;
  transition: all .25s;
  
  &:hover{
    border-color: #444757;
    background-color: #1d1e2b;
  }
  
   &:focus{
    outline:none;
    border-color: #ff6f26; 
    background-color: #08080d;
  }
  
  &::placeholder{
    font-family: "NotoSansCJKL";
    color: #9698a3;
    font-size: 12px;
  }
`

const SearchInput = (props: { value: string, changeValue: Function, enterEvent:Function }) => {

    return (
        <StyledSearchInput value={props.value}
                           onChange={e => props.changeValue(e)}
                           onKeyDown={(e)=>{
                               if(e.code === 'Enter') {
                                   props.enterEvent(e)
                               }
                           }}
                           placeholder={"CCTV명을 입력해 주세요"}/>
    )

}

export default SearchInput;