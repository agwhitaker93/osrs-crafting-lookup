(ns osrs-crafting-lookup.components.search
  (:require [ring.util.response :refer [response]]
            [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]))

(def base "http://services.runescape.com/m=itemdb_oldschool/api/catalogue")

(def category-id 1)                                         ; only 1 category in osrs

(def max-pages 12)                                          ; allegedly exactly 12 pages per "alpha" value

(defn detail-url [item-id]
  (str base "/detail.json?item=" item-id))

(defn category-url []
  (str base "/category.json?category=" category-id))

(defn items-url [item-letter page]
  (str base "/items.json?category=" category-id "&alpha=" item-letter "&page=" page))

(defn rs-lookup [query]
  (-> query
      (seq)
      (nth 0)
      (items-url 1)
      (client/get)
      (:body)
      (parse-string true)
      (get :items)))

(defn handle [query]
  (-> (rs-lookup query)
      response
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
