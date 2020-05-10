(ns osrs-crafting-lookup.migrations
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [osrs-crafting-lookup.config :refer [db]]
            [environ.core :refer [env]]))

(def config {:datastore  (jdbc/sql-database db)
             :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate config))

(defn rollback []
  (repl/rollback config))

(if (not (= (env :env) "dev"))
  (do
    (println "Detected we're not in dev mode, running migrations")
    (migrate)))
