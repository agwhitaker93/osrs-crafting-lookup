(ns osrs-crafting-lookup.config
  (:require [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]))

(defn config []
  {:http-port  (Integer. (or (env :port) 10555))
   :middleware [[wrap-defaults api-defaults]
                wrap-with-logger
                wrap-gzip]})

(def ge-api-base-url "http://services.runescape.com/m=itemdb_oldschool/api/catalogue")

(def wiki-base-url "https://oldschool.runescape.wiki/w")

(def db (if (= (env :env) "dev")
          {:dbtype "postgresql"
           :dbname "runescape"
           :host "localhost"
           :user "postgres"
           :password (env :db-password)}
          (env :database-url "postgres://localhost:5432/runescape")))
