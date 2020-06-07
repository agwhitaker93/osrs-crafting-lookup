(ns osrs-crafting-lookup.components.items
  (:require [ring.util.response :refer [response]]
            [clojure.string :refer [lower-case starts-with?]]
            [osrs-crafting-lookup.components.http-client :refer [get-with-retry]]
            [osrs-crafting-lookup.config :refer [ge-api-base-url]]
            [cheshire.core :refer [parse-string]]))

(def category-id 1)                                         ; only 1 category in osrs

(defn items-url [item-char page]
  (str ge-api-base-url "/items.json?category=" category-id "&alpha=" item-char "&page=" page))

(defn- get-page [item-char page]
  (-> (items-url item-char page)
      (get-with-retry)
      (:body)
      (parse-string true)
      (:items)))

(defn- get-all-pages [item-char]
  (loop [page 1
         page-results '()]
    (let [result (get-page item-char page)]
      (if (or (nil? result) (empty? result))
        page-results
        (recur (inc page) (flatten (conj page-results result)))))))

(defn- narrow-selection [item-name results]
  (filter #(-> %1
               :name
               (lower-case)
               (starts-with? item-name)) results))

(defn- rs-lookup [query get-fn]
  (let [lower-cased (lower-case query)
        first-char (first (seq lower-cased))
        query-pages (get-fn first-char)]
    (narrow-selection lower-cased query-pages)))

(defn handle [{query :query page :page}]
  (let [get-fn (if (nil? page)
                 get-all-pages
                 #(get-page %1 page))]
    (-> (rs-lookup query get-fn)
        (response)
        (assoc :headers {"Content-Type" "text/html; charset=utf-8"}))))
