(ns good-api.system
  (:require [aleph.http :as http]
            [com.stuartsierra.component :as component]
            [good-api.api :as api]))

(defrecord WebServer [port handler server]
  component/Lifecycle
  (start [component]
    (let [server (http/start-server handler {:port port :join? false})]
      (assoc component :server server)))
  (stop [component]
    (when server
      (.close server)
      component)))

(defn server-component [port db]
  (map->WebServer {:port port :handler (api/handler-routes db)}))

(defrecord DbComponent [connection]
  component/Lifecycle
  (start [component]
    (assoc component :connection connection))
  (stop [component]
    (dissoc component :connection)))

(defn db-component [connection]
  (->DbComponent connection))


(defn system [config]
  (let [{:keys [connection port]} config
        db (db-component connection)]
    (-> (component/system-map
          :db db
          :app (server-component port db))
      (component/system-using
          {:app {:database :db}}))))
