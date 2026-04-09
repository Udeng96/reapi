import { useEffect, useRef } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { eventApi } from "../../entities/event/api";
import { eventKeys } from "../../entities/event/queries";
import { mobileKeys } from "../../entities/mobile/queries";
import { useEventStore } from "../../entities/event/store";
import { useMapStore } from "../../entities/map/store";

const WS_URL =
  import.meta.env.VITE_WS_URL ??
  (import.meta.env.PROD ? `ws://${window.location.host}/api-ws` : "ws://localhost:26000/websocket");

export function useEventWebSocket() {
  const queryClient = useQueryClient();
  const setActiveItem = useEventStore((s) => s.setActiveItem);
  const setMapMove = useMapStore((s) => s.setMapMove);

  // wsEventReceive는 메시지 처리 시점에 최신값을 읽어야 하므로 ref로 관리
  const wsEventReceiveRef = useRef(useEventStore.getState().wsEventReceive);
  useEffect(
    () =>
      useEventStore.subscribe((s) => {
        wsEventReceiveRef.current = s.wsEventReceive;
      }),
    [],
  );

  useEffect(() => {
    let ws: WebSocket;
    let reconnectTimer: ReturnType<typeof setTimeout>;
    let destroyed = false;

    function connect() {
      ws = new WebSocket(WS_URL);

      ws.onmessage = async (e) => {
        const data = JSON.parse(e.data) as {
          data: string;
          uSvcOutbId?: string;
          statEvetId?: string;
        };

        if (data.data === "mobile") {
          queryClient.invalidateQueries({ queryKey: mobileKeys.live() });
          queryClient.invalidateQueries({ queryKey: mobileKeys.pasts() });
        } else {
          if (!wsEventReceiveRef.current) return;
          if (!data.uSvcOutbId) return;

          const detail = await eventApi.fetchEventByUSvcId(data.uSvcOutbId);
          if (!detail) return;

          queryClient.invalidateQueries({ queryKey: eventKeys.lists() });
          queryClient.setQueryData(eventKeys.detail(detail.statEvetOutbSeqn), detail);
          setActiveItem(detail.statEvetOutbSeqn);

          if (detail.lat && detail.lng) {
            setMapMove({
              latlng: [parseFloat(detail.lat), parseFloat(detail.lng)],
              type: "event",
            });
          }
        }
      };

      ws.onclose = (e) => {
        if (!destroyed && e.code !== 1005) {
          reconnectTimer = setTimeout(connect, 1000);
        }
      };
    }

    connect();

    return () => {
      destroyed = true;
      clearTimeout(reconnectTimer);
      ws?.close(1000);
    };
  }, [queryClient, setActiveItem, setMapMove]);
}
