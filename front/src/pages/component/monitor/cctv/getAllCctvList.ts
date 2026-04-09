import {CctvInfo} from "../../../../data/interface/monitor/monitorInterface";

// 재생되는 모든 cctv list
export const GetAllCctvList = (cctvAmount : number, cctvGrid :number, cctvList:Array<CctvInfo|null>) => {

    let Gcd = 0;

    //최대 공약수
    for (let  i = 0; i<=cctvAmount; i++){
        if(cctvAmount%i===0 && cctvGrid%i===0){
            Gcd = i
        }
    }

    //최소 공배수

    let Lcm = Gcd * (cctvAmount/Gcd) * (cctvGrid/Gcd)

    // 전환하는 총 페이지
    let allCyclePageCnt = Lcm / cctvAmount
    let allPageCctvList:Array<CctvInfo|null> = []

    for(let i = 0; i<allCyclePageCnt; i++){
        cctvList.map((item)=>{
            allPageCctvList.push(item);
        })
    }

    return allPageCctvList

}

// 페이지별 재생되는 cctv list 나누는 작업
export const GetAllPagePlayList = (allPlayList:Array<CctvInfo|null>, gridAmount:number) => {

    let start = 0;
    let end = 0;
    let lastAmount = allPlayList.length;

    let allPagePlayList : Array<Array<CctvInfo|null>> = []

    while(lastAmount!==0){

        let newAllPagePlayList:Array<CctvInfo|null> = []

        end += gridAmount

        for(let i = start; i<end; i++){
            newAllPagePlayList.push(allPlayList[i]);
            start++;
        }

        allPagePlayList.push(newAllPagePlayList)
        lastAmount = lastAmount- gridAmount
    }
    return allPagePlayList;
}
