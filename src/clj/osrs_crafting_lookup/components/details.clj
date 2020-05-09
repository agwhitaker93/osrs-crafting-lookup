(ns osrs-crafting-lookup.components.details
  (:require [ring.util.response :refer [response]]
            [clojure.string :refer [lower-case starts-with?]]
            [clj-http.client :as client]
            [osrs-crafting-lookup.config :refer [base-url]]
            [cheshire.core :refer [parse-string]]))

(defn detail-url [item-id]
  (str base-url "/detail.json?item=" item-id))

(defn handle [& args]
  (println "Not implemented yet"))
