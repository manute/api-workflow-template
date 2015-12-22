(ns good-api.core
  (:require [aleph.http :as http]
            [com.stuartsierra.component :as component]
            [yesql.core :as yesql]
            [bidi.ring :as bidi]))

;; YESQL BUG - put in another namespace
;; https://github.com/clojars/clojars-web/blob/master/src/clojars/db/sql.clj
;;
(yesql/defquery select-accounts "db/queries.sql")



(def DEV-DB-SPEC
  "jdbc:postgresql://10.19.6.19:3019/xander?user=malonso&password=LjRTBb+YdF+K0c1q")

(defn get-accounts [db]
  (select-accounts {} db))

(defn handler-routes [db]
  (bidi/make-handler ["/" {"account"
                           {:get
                            {"" (fn [req] {:status 200 :body (get-accounts db)})}}}]))

(defn wrapper-handler [db]
  (let [accounts (get-accounts db)]
    (fn [req]
      {:status 200
       :headers {"content-type" "text/plain"}
       :body accounts})))

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
  (map->WebServer {:port port :handler (handler-routes db)}))

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

;; EXAMPLE COMPONENT
;;
;; start
;;-----------------
;; (def example (system {:port 8080 :connection DEV}))
;; (alter-var-root #'good-api.core/example component/start)

;; stop
;;--------------------
;;  (alter-var-root #'good-api.core/example component/stop))
