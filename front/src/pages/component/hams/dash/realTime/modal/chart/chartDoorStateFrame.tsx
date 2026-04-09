import * as React from 'react';
import {useEffect, useState} from 'react';
import * as Highcharts from "highcharts";
import HighchartsReact from "highcharts-react-official/dist/highcharts-react";
import {useDispatch, useSelector} from "react-redux";
import {StatOptions, StatParam} from "../../../../../../../data/interface/hams/hamsInterface";
import * as hamsActions from "../../../../../../saga/action/hams/hams";
import {RootState} from "../../../../../../store/rootStore";
import {HAMS_STAT_D_EXAMPLE, HAMS_STAT_M_EXAMPLE, HAMS_STAT_Y_EXAMPLE} from "../../../../../../../data/const/hamsConst";


const ChartDoorStateFrame = (props: { type: string, hamsId: string, isOpen: boolean }) => {

    const storeHamsDailyStat = useSelector((state: RootState) => state.server.hams.hamsDailyStat)
    const storeHamsMonthlyStat = useSelector((state: RootState) => state.server.hams.hamsMonthlyStat)
    const storeHamsByYearStat = useSelector((state: RootState) => state.server.hams.hamsByYearStat)


    const dispatch = useDispatch()
    const option: StatOptions = {doorType: "1", frontBack: "1", dateType: props.type, startDtm: null}
    const doorStat = props.type === "D" ? storeHamsDailyStat : props.type === "M" ? storeHamsMonthlyStat : storeHamsByYearStat

    const pointWidth =
        props.type === "D" ? 8 : props.type === "M" ? 20 : 25


    const [categoryList, setCategoryList] = useState<string[]>([])
    const [openDataList, setOpenDataList] = useState<Array<number>>([])
    const [closeDataList, setCloseDataList] = useState<Array<number>>([])

    useEffect(() => {
        //개발용 임의의 id => 수정 필요
        let param: StatParam = {hamsId: props.hamsId, param: option}

        if ((props.hamsId !== ("" && "HAMS")) && props.isOpen) {
            dispatch(hamsActions.actions.requestDoorStat(param))
        }
    }, [props.hamsId, props.isOpen])

    let categories: string[] = []
    let openCnts: number[] = []
    let closeCnts: number[] = []

    useEffect(() => {

        doorStat.map((item) => {
            categories.push(item.date)
            openCnts.push(item.openCnt ? item.openCnt : 0)
            closeCnts.push(item.clsCnt ? item.clsCnt : 0)
        })

        setCategoryList(categories)
        setOpenDataList(openCnts)
        setCloseDataList(closeCnts)


    }, [doorStat])


    const options = {
        chart: {
            type: 'column',
            backgroundColor: 'transparent',
            spacingTop: 72,
            spacingBottom: 36.7,
            spacingLeft: 31.2,
            spacingRight: 56,
            width: 1600,
            height: 284
        },
        title: {
            text: '',
        },
        subtitle: {
            text: ''
        },
        credits: {
            enabled: false
        },
        tooltip: {
            style: {
                fontFamily: 'NotoSansCJKR',
                color: '#fff',
                fontSize: 11,
            },
            backgroundColor: '#15161f',
            borderColor: '#ff6f26',
            borderRadius: 10,
            shared: true
        },
        xAxis: {
            tickColor: 'transparent',
            lineColor: '#40424f',
            lineWidth: 1,
            labels: {
                style: {
                    fontFamily: 'NotoSansCJKL',
                    color: '#c9cbd6',
                    fontSize: 10,
                }
            },
            categories: categoryList,
        },
        yAxis: {
            title: {
                text: ''
            },
            gridLineColor: '#353745',
            gridLineWidth: 1,
            labels: {
                style: {
                    fontFamily: 'NotoSansCJKL',
                    color: '#777987',
                    fontSize: 10,
                }
            },
            // tickPositions: [0, 5, 10, 15, 20, 25],
        },
        legend: {
            floating: true,
            layout: 'horizontal',
            align: 'left',
            verticalAlign: 'top',
            itemStyle: {
                fontFamily: 'NotoSansCJKL',
                color: '#dfe0e8',
                fontSize: 12,
            },
            itemHoverStyle: {
                color: '#fff'
            },
            symbolRadius: 3,
            symbolWidth: 12,
            symbolHeight: 12,
            y: -51,
            x: 140
        },
        series: [{
            name: '열림',
            data: openDataList,
            color: '#f06969'
        }, {
            name: '닫힘',
            data: closeDataList,
            color: '#3acef0'
        }],
        plotOptions: {
            column: {
                borderWidth: 0,
                pointPadding: 0,
                borderRadius: 0,
            },
            series: {
                pointWidth: pointWidth,
            },
        },
    }

    return (
        <div className="chart_frame">
            <div
                className={`chart_title ${props.type === "D" ? "daily" : props.type === "M" ? "monthly" : "by_year"}_title`}>
                <span>
                    {
                        props.type === "D" ? "일별" : props.type === "M" ? "월별" : "년별"
                    }
                </span>앞문 개폐 수
            </div>
            {
                doorStat ?
                    <div className="chart_in">
                        <HighchartsReact highcharts={Highcharts} options={options}/>
                    </div>
                    :
                    <div className="chart_load_error_area">
                        <button type="button" className="btn_reload"/>
                        <p className="chart_load_error_message"><span>통계 차트 로드에 문제가 발생하였습니다.</span></p>
                    </div>
            }
        </div>
    )

}

export default ChartDoorStateFrame