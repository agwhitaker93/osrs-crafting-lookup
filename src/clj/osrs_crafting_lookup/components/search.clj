(ns osrs-crafting-lookup.components.search
  (:require [ring.util.response :refer [response]]))

(defn handle []
  (-> "<h1>It's the API but from a separate file</h1>"
      response
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
