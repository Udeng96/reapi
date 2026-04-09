import {DashAreaProps, HamsGisFacInfo} from "../interface/hams/hamsInterface";

export const HAMS_DASH_INFO_TYPE : Array<DashAreaProps> = [
    {
        id : "title_address",
        nm : "주소"
    },
    {
        id : "title_group",
        nm : "그룹"
    },
    {

        id: "title_coordinate",
        nm : "위경도"
    }
]

export const HAMS_REAL_TIME_BOX_TYPE =[

    {
        typeId : 'doorLock',
        typeNm : '앞문 도어락 상태',
        titleStyle : '106px',
        value : {
            LOCK : "‘잠금중’ 입니다",
            UNLOCK : "‘잠금해제 중’ 입니다",
            NONE : "데이터 값이 없습니다"
        }
    },
    {
        typeId : 'doorState',
        typeNm : '앞문 개폐 상태',
        titleStyle : '106px',
        value : {
            OPEN : "문이 ‘열려’있습니다",
            CLOSE : "문이 ‘닫혀’있습니다",
            NONE : "데이터 값이 없습니다"
        }
    },
    {
        typeId: 'connection',
        typeNm: '연결 상태',
        titleStyle : '72px',
        value:{
            CONN : "‘연결’ 되었습니다",
            DISCONN : "‘미연결’ 되었습니다",
            NONE :  "데이터 값이 없습니다"
        }
    }
    ,


]

// 원래 enum 으로 할까 했는데 enum은 0 부터 시작
export const HAMS_DOOR_LOCK_TYPE = {

    NONE : "1",
    UNLOCK : "2",
    LOCK : "3",
    NORECOG : "4",
    ERROR : "5"

}

export const HAMS_DOOR_STATE_TYPE = {

    NONE : "1",
    OPEN : "2",
    CLOSE : "3"

}

export const HAMS_REAL_TIME_VALUE_COLOR = {

    tRed: "#f06969",
    tSky: "#3acef0",
    tMint: "#42e3c5",
    tPurple: "#ba3ce8",
    tGray: "#777987",

}

// todo :  테스트 용 예제 데이터
export const HAMS_LIST_EXAMPLE : Array<HamsGisFacInfo>= [
    {
        facId : 'HAMS_example_hwaseong_001',
        facNm : '화성시청',
        addr : '경기도 화성시 남양읍 시청로',
        lat : 126.84031290393,
        lng : 37.190041275158,
        clfyCd : "001",
        clfyNm : 'example1',
        useYn : 1,
        updDtm : 20221215,
        data : null ,
        hamsId : "HAMS_001",
        groupNm : "grp_001",
        ip : null,
        port : null,
        lowFacInfos : null,
        status : {
            frontDoor : "3",
            frontDoorLock : "3",
            backDoor : "2",
            backDoorLock : "2",
            temp : 10,
            humi : 25,
            isConnect : true
        }
    },
    {
        facId : 'HAMS_example_hwaseong_002',
        facNm : '무인국사_서신면',
        addr : '경기도 화성시 서신면',
        lat : 126.67031290393,
        lng : 37.190041275158,
        clfyCd : "002",
        clfyNm : 'example2',
        useYn : 1,
        updDtm : 20221213,
        data : null ,
        hamsId : "HAMS_002",
        groupNm : "grp_002",
        ip : null,
        port : null,
        lowFacInfos : null,
        status : {
            frontDoor : "2",
            frontDoorLock : "2",
            backDoor : "3",
            backDoorLock : "3",
            temp : 5,
            humi : 15,
            isConnect : true
        }
    },
    {
        facId : 'HAMS_example_hwaseong_003',
        facNm : '무인국사_향남읍',
        addr : '경기도 화성시 향남읍',
        lat : 126.90031290393,
        lng : 37.140041275158,
        clfyCd : "003",
        clfyNm : 'example3',
        useYn : 1,
        updDtm : 20221213,
        data : null ,
        hamsId : "hams_003",
        groupNm : "grp_003",
        ip : null,
        port : null,
        lowFacInfos : null,
        status : {
            frontDoor : "2",
            frontDoorLock : "3",
            backDoor : "3",
            backDoorLock : "2",
            temp : 5,
            humi : 2.5,
            isConnect : true
        }
    },
    {
        facId : 'HAMS_example_hwaseong_004',
        facNm : '관리마을회관',
        addr : '경기도 화성시 향남읍 관리마을회관',
        lat : 126.95031290393,
        lng : 37.130041275158,
        clfyCd : "004",
        clfyNm : 'example4',
        useYn : 1,
        updDtm : 20221211,
        data : null ,
        hamsId : "hams_004",
        groupNm : "grp_004",
        ip : null,
        port : null,
        lowFacInfos : null,
        status : {
            frontDoor : "3",
            frontDoorLock : "2",
            backDoor : "2",
            backDoorLock : "2",
            temp : 21,
            humi : 22.5,
            isConnect : true
        }
    },
    {
        facId : 'HAMS_example_hwaseong_005',
        facNm : '무인국사_장안면',
        addr : '경기도 화성시 장안면',
        lat : 126.84031290393,
        lng : 37.070041275158,
        clfyCd : "005",
        clfyNm : 'example5',
        useYn : 1,
        updDtm : 20221213,
        data : null ,
        hamsId : "hams_005",
        groupNm : "grp_005",
        ip : null,
        port : null,
        lowFacInfos : null,
        status : {
            frontDoor : "2",
            frontDoorLock : "3",
            backDoor : "3",
            backDoorLock : "2",
            temp : 32,
            humi : 5,
            isConnect : true
        }
    },
    {
        facId : 'HAMS_example_hwaseong_006',
        facNm : '우정공업단지',
        addr : '경기도 화성시 우정읍 우정공업단지',
        lat : 126.80689246,
        lng : 37.08601875,
        clfyCd : "006",
        clfyNm : 'example6',
        useYn : 1,
        updDtm : 20221209,
        data : null ,
        hamsId : "hams_006",
        groupNm : "grp_006",
        ip : null,
        port : null,
        lowFacInfos : null,
        status : {
            frontDoor : "2",
            frontDoorLock : "2",
            backDoor : "2",
            backDoorLock : "2",
            temp : -5,
            humi : 0.5,
            isConnect : true
        }
    }

]

export const HAMS_STAT_D_EXAMPLE = [
    {
        date:'2022-11-30',
        cnt : 30,
        openCnt : 12,
        clsCnt : 5,
        noneCnt : 13
    },
    {
        date:'2022-12-01',
        cnt : 22,
        openCnt : 12,
        clsCnt : 5,
        noneCnt : 5
    },
    {
        date:'2022-12-02',
        cnt : 0,
        openCnt : 0,
        clsCnt : 0,
        noneCnt : 0
    },
    {
        date:'2022-12-03',
        cnt : 8,
        openCnt : 1,
        clsCnt : 7,
        noneCnt : 0
    },
    {
        date:'2022-12-04',
        cnt : 29,
        openCnt : 11,
        clsCnt : 13,
        noneCnt : 5
    },
    {
        date:'2022-12-05',
        cnt : 55,
        openCnt : 20,
        clsCnt : 22,
        noneCnt : 13
    },
    {
        date:'2022-12-06',
        cnt : 10,
        openCnt : 4,
        clsCnt : 4,
        noneCnt : 2
    },
    {
        date:'2022-12-07',
        cnt : 30,
        openCnt : 13,
        clsCnt : 1,
        noneCnt : 16
    },
    {
        date:'2022-12-08',
        cnt : 17,
        openCnt :10,
        clsCnt : 2,
        noneCnt : 5
    },
    {
        date:'2022-12-09',
        cnt : 44,
        openCnt : 11,
        clsCnt : 33,
        noneCnt : 0
    },
    {
        date:'2022-12-10',
        cnt : 13,
        openCnt : 0,
        clsCnt : 13,
        noneCnt : 0
    },
    {
        date:'2022-12-11',
        cnt : 21,
        openCnt : 12,
        clsCnt : 9,
        noneCnt : 0
    },
    {
        date:'2022-12-12',
        cnt : 20,
        openCnt : 10,
        clsCnt : 10,
        noneCnt : 0
    },
    {
        date:'2022-12-13',
        cnt : 5,
        openCnt : 1,
        clsCnt : 2,
        noneCnt : 2
    },
    {
        date:'2022-12-14',
        cnt : 45,
        openCnt : 12,
        clsCnt : 13,
        noneCnt : 20
    },
    {
        date:'2022-12-15',
        cnt : 33,
        openCnt : 13,
        clsCnt : 7,
        noneCnt : 10
    },
    {
        date:'2022-12-16',
        cnt : 54,
        openCnt :22,
        clsCnt : 25,
        noneCnt : 7
    },
    {
        date:'2022-12-17',
        cnt : 9,
        openCnt : 3,
        clsCnt : 4,
        noneCnt : 2
    },
    {
        date:'2022-12-18',
        cnt : 19,
        openCnt : 12,
        clsCnt : 7,
        noneCnt : 0
    },
    {
        date:'2022-12-19',
        cnt : 22,
        openCnt : 4,
        clsCnt : 8,
        noneCnt : 10
    },
    {
        date:'2022-12-20',
        cnt : 34,
        openCnt : 21,
        clsCnt : 13,
        noneCnt : 0
    },
    {
        date:'2022-12-21',
        cnt : 21,
        openCnt : 7,
        clsCnt : 7,
        noneCnt : 7
    }


]


export const HAMS_STAT_M_EXAMPLE = [
    {
        date : '2022-01',
        cnt : 230,
        openCnt : 122,
        clsCnt : 83,
        noneCnt : 25
    },
    {
        date : '2022-02',
        cnt : 300,
        openCnt : 100,
        clsCnt : 150,
        noneCnt : 50
    },
    {
        date : '2022-03',
        cnt : 177,
        openCnt : 107,
        clsCnt : 70,
        noneCnt : 0
    },
    {
        date : '2022-04',
        cnt : 255,
        openCnt : 122,
        clsCnt : 83,
        noneCnt : 50
    },
    {
        date : '2022-05',
        cnt : 345,
        openCnt : 187,
        clsCnt : 58,
        noneCnt : 100
    },
    {
        date : '2022-06',
        cnt : 77,
        openCnt : 30,
        clsCnt : 47,
        noneCnt : 0
    },
    {
        date : '2022-07',
        cnt : 355,
        openCnt : 105,
        clsCnt : 200,
        noneCnt : 50
    },
    {
        date : '2022-08',
        cnt : 370,
        openCnt : 122,
        clsCnt : 103,
        noneCnt : 145
    },
    {
        date : '2022-09',
        cnt : 100,
        openCnt : 50,
        clsCnt : 50,
        noneCnt : 0
    },
    {
        date : '2022-10',
        cnt : 230,
        openCnt : 122,
        clsCnt : 83,
        noneCnt : 25
    },
    {
        date : '2022-11',
        cnt : 200,
        openCnt : 80,
        clsCnt : 80,
        noneCnt : 40
    },
    {
        date : '2022-12',
        cnt : 400,
        openCnt : 252,
        clsCnt : 103,
        noneCnt : 45
    },
]

export const HAMS_STAT_Y_EXAMPLE = [
    {
        date: '2021',
        cnt : 2350,
        openCnt : 1330,
        clsCnt : 1000,
        noneCnt : 20,
    },
    {
        date : '2022',
        cnt : 5005,
        openCnt : 2102,
        clsCnt : 2303,
        noneCnt : 560,

    }
]