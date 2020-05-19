(ns scratch
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.components.items :as items]
            [clojure.walk :refer [keywordize-keys]]))

(defn magic! []
  (->> (read-dir craftables-dir)
       (map keywordize-keys)
       (map #(pr-edn (str "resources/keywordized_craftable/" (:name %1) ".edn") %1))))

(defn transform! []
  (->> [{} {}]
       (map #(assoc %1 :infobox (reduce merge {} (:infobox %1))))
       (filter #(contains? (:infobox %1) :name))
       (map #(assoc %1 :meta (:infobox %1)))
       (map #(dissoc %1 :infobox :name))
       (map #(assoc %1 :meta (dissoc (:meta %1) :placeholder :noteable :destroy :update :weight :equipable)))
       (map #(assoc-in %1 [:meta :ha-value] (* (read-string (:value (:meta %1))) 0.6)))
       (map #(assoc-in %1 [:meta :la-value] (* (read-string (:value (:meta %1))) 0.4)))
       (map #(assoc-in %1 [:meta :ge-value] "0"))))
