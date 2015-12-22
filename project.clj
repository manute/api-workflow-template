(defproject good-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [aleph "0.4.1-beta2"]
                 [com.stuartsierra/component "0.3.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [yesql "0.5.1"]
                 [bidi "1.24.0"]
                 [environ "1.0.1"]]

  :plugins [[lein-environ "1.0.1"]]

  ;; NEED TO HAS A file profiles.clj in the project dir for the envs like :
  ;; {:dev {:source-paths ["dev"]
  ;;      :dependencies [[org.clojure/tools.namespace "0.2.11"]
  ;;                     [org.clojure/java.classpath "0.2.3"]]
  ;;      :env {:database-url "jdbc:postgresql://host:port/dbname?user=username&password=secret"
  ;;            :server-port 8080}}}

  )
