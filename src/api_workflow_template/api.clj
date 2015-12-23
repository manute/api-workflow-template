(ns api-workflow-template.api
  (:require [yesql.core :as yesql]
            [bidi.ring :as bidi]))

;; YESQL BUG - put in another namespace
;; https://github.com/clojars/clojars-web/blob/master/src/clojars/db/sql.clj
;;
(yesql/defquery select-accounts "db/queries.sql")


(defn get-accounts [db]
  (select-accounts {} db))

(defn handler-routes [db]
  (bidi/make-handler ["/" {"account"
                           {:get
                            {"" (fn [req] {:status 200 :body (get-accounts db)})}}}]))
