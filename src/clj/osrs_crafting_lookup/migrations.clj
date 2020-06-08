(ns osrs-crafting-lookup.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [osrs-crafting-lookup.config :refer [db]]
            [environ.core :refer [env]]))

(defn config [db-conf]
  {:datastore  (jdbc/sql-database db-conf)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate [db-conf]
  (repl/migrate (config db-conf)))

(defn rollback [db-conf]
  (repl/rollback (config db-conf)))

(if (not (= (env :env) "dev"))
  (do
    (println "Detected we're not in dev mode, running migrations")
    (migrate db))) ; fallback on db-conf from config ns
