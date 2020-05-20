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

(defn list-files [dir]
  "file-seq returns the dir given as the first entry"
  (rest (file-seq (clojure.java.io/file dir))))

(defn read-file [file]
  (read-string (slurp file)))

(defn read-dir [dir]
  (map read-file (list-files dir)))

(def craftables-dir "resources/craftable")

(def db (if (= (env :env) "dev")
          {:dbtype "postgresql"
           :dbname "runescape"
           :host "localhost"
           :user "postgres"
           :password (env :db-password)}
          (env :database-url "postgres://localhost:5432/runescape")))
