import React from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import DstrApp from "./App";

// 공통 스타일
import "react-datepicker/dist/react-datepicker.css";
import "./styles/reset.css";
import "./styles/fontStyle.css";
import "./styles/common.css";
import "./styles/modalPopup.css";
import "./styles/reactDatePicker.css";

// GIS / 재난상황 스타일
import "./styles/gis/dashContainer.css";
import "./styles/gis/gis.css";
import "./styles/gis/gisIcon.css";
import "./styles/gis/gisPopup.css";
import "./styles/gis/disasterEventWrap.css";
import "./styles/gis/cctvSearchWrap.css";
import "./styles/gis/cctvContainer.css";
import "./styles/gis/cctvFullPopup.css";
import "./styles/gis/mobileVideoWrap.css";
import "./styles/gis/mobileFullPopup.css";
import "./styles/gis/pastEvtHistoryPopup.css";
import "./styles/gis/pagination.css";
import "./styles/gis/checkBox.css";
import "./styles/gis/radioButton.css";
import "./styles/gis/tooltip.css";
import "./styles/gis/scrollStyle.css";
import "./styles/gis/ripple.css";

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <DstrApp />
    </QueryClientProvider>
  </React.StrictMode>,
);
