import React from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import { useHamsDoorStat } from '../../entities/hams/queries';
import type { DateType } from '../../entities/hams/types';

interface Props {
  hamsId: string;
  dateType: DateType;
}

const TITLE_CLASS: Record<DateType, string> = {
  D: 'daily_title',
  M: 'monthly_title',
  Y: 'by_year_title',
};

const DATE_TYPE_LABEL: Record<DateType, string> = {
  D: '일별',
  M: '월별',
  Y: '년별',
};

const legendDotStyle: React.CSSProperties = {
  display: 'inline-block',
  width: 12,
  height: 12,
  borderRadius: 3,
  marginRight: 4,
};

const legendItemStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  fontFamily: 'NotoSansCJKL',
  fontSize: 12,
  color: '#dfe0e8',
};

// spacingTop:72, spacingBottom:36.7, spacingLeft:31.2, spacingRight:56 (Highcharts 기준)
// chart_in을 top:62px에서 시작 (padding 18 + title 32 + gap 12)
// margin.top은 SVG 내 여백만 — 차트 크기는 유지
const CHART_IN_TOP = 80;
const CHART_MARGIN = { top: 12, right: 56, left: 50, bottom: 8 };
const YAXIS_WIDTH = 31;

// pointWidth: D=8, M=20, Y=25 (Highcharts pointWidth 그대로)
const BAR_SIZE: Record<DateType, number> = { D: 8, M: 20, Y: 25 };

export function DoorStatChart({ hamsId, dateType }: Props) {
  const { data, isError } = useHamsDoorStat(
    hamsId || null,
    {
      doorType: 'FRONT',
      frontBack: 'F',
      dateType,
      startDtm: null,
    }
  );

  return (
    <div className="chart_frame">
      {/* 제목 + 범례 — position:relative + zIndex:1 로 chart_in(absolute) 위에 표시 */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
        <p className={`chart_title ${TITLE_CLASS[dateType]}`}>
          <span>{DATE_TYPE_LABEL[dateType]}</span>앞문 개폐 수
        </p>
        <span style={legendItemStyle}>
          <i style={{ ...legendDotStyle, background: '#f06969' }} />
          열림
        </span>
        <span style={legendItemStyle}>
          <i style={{ ...legendDotStyle, background: '#3acef0' }} />
          닫힘
        </span>
      </div>

      {isError ? (
        <div className="chart_load_error_area">
          <button type="button" className="btn_reload" />
          <p className="chart_load_error_message">
            <span>통계 차트 로드에 문제가 발생하였습니다.</span>
          </p>
        </div>
      ) : (
        // chart_in: position absolute, top:0, width/height 100% → SVG가 chart_frame 전체를 덮음
        // recharts margin.top:66 으로 제목행 아래부터 plot 시작
        <div className="chart_in" style={{ top: CHART_IN_TOP, height: `calc(100% - ${CHART_IN_TOP}px)` }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart
              data={data ?? []}
              margin={CHART_MARGIN}
              barGap={0}
              barCategoryGap={BAR_SIZE[dateType] * 2}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#353745" />
              <XAxis
                dataKey="date"
                tick={{ fill: '#c9cbd6', fontSize: 10, fontFamily: 'NotoSansCJKL' }}
                tickLine={false}
                axisLine={{ stroke: '#40424f' }}
              />
              <YAxis
                width={YAXIS_WIDTH}
                allowDecimals={false}
                tick={{ fill: '#777987', fontSize: 10, fontFamily: 'NotoSansCJKL' }}
                axisLine={false}
                tickLine={false}
              />
              <Tooltip
                cursor={false}
                contentStyle={{
                  backgroundColor: '#15161f',
                  border: '1px solid #ff6f26',
                  borderRadius: 10,
                  color: '#fff',
                  fontSize: 11,
                }}
              />
              <Bar dataKey="openCnt" name="열림" fill="#f06969" barSize={BAR_SIZE[dateType]} />
              <Bar dataKey="clsCnt" name="닫힘" fill="#3acef0" barSize={BAR_SIZE[dateType]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}
