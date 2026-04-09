import RealtimeDataBox from "./realtimeDataBox";

const RealTimeArea = () => {

    return (
        // realtimeDataFrame
        <div className="realtime_data_frame">
            <p className="info_title title_realtime_data">항목별 실시간 현황</p>
            <div className="realtime_data_box_group">
                {/* 테스트 후 isLock props 삭제*/}
                <RealtimeDataBox type={'doorLock'} isSep={false}/>
                <RealtimeDataBox type={'doorState'} isSep={false}/>
                <RealtimeDataBox type={'connection'} isSep={false}/>
                <RealtimeDataBox type={''} isSep={true}/>
            </div>
        </div>
    )

}

export default RealTimeArea