(ns craftables
  (:require [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string])
  (:import (java.io File StringWriter)
           (java.net URLEncoder)))

(def source "resources/scraped-craftable")

(def keywordize-target "resources/keywordized-craftable")

(def transformed-target "resources/craftable")

(defn list-files [dir]
  "file-seq returns the dir given as the first entry"
  (rest (file-seq (clojure.java.io/file dir))))

(defn read-file [file]
  (read-string (slurp file)))

(defn read-dir [dir]
  (map read-file (list-files dir)))

(defn pr-edn [target filename content]
  (if (not (.exists (io/file target)))
    (.mkdirs (new File target)))
  (spit (str target "/" filename ".edn") (let [w (new StringWriter)]
                                           (pprint content w)
                                           (.toString w))))

(defn flatten-infobox [craftable]
  (if (= (count (get craftable :infobox)) 1)
    (assoc craftable :infobox (reduce merge {} (:infobox craftable)))
    (do
      (println "This entry has multiple entries for infobox: " (get craftable :name))
      craftable)))

(defn keywordize-craftables []
  (->> (read-dir source)
       (map keywordize-keys)
       (map flatten-infobox)
       (map #(pr-edn keywordize-target (:name %1) %1))))

(def base-icon-url "http://services.runescape.com/m=itemdb_oldschool/1588586637705_obj_sprite.gif?id=")

(def base-icon-large-url "http://services.runescape.com/m=itemdb_oldschool/1588586637705_obj_big.gif?id=")

(defn static-vals [id name]
  {:ge_value   "0"
   :icon       (str base-icon-url id)
   :icon_large (str base-icon-large-url id)
   :wiki       (str "https://oldschool.runescape.wiki/w/" (URLEncoder/encode (string/replace (str name) " " "_") "UTF-8"))})

(def required [:id :name :examine :tradable :value :members-only])

(def required-from->to {#(Integer/parseInt (clojure.string/replace (or
                                                                    (get-in %1 [:infobox :id])
                                                                    (get-in %1 [:infobox :id1])) "," "")) :id
                        #(or
                          (get-in %1 [:name])
                          (get-in %1 [:infobox :name])
                          (get-in %1 [:infobox :name1]))                                                  :name
                        #(or
                          (get-in %1 [:infobox :examine])
                          (get-in %1 [:infobox :examine1]))                                               :examine
                        #(= "Yes" (or
                                   (get-in %1 [:infobox :tradeable])
                                   (get-in %1 [:infobox :tradeable1])))                                   :tradable
                        #(or
                          (get-in %1 [:infobox :value])
                          (get-in %1 [:infobox :value1]))                                                 :value
                        #(= "Yes" (or
                                   (get-in %1 [:infobox :members])
                                   (get-in %1 [:infobox :members1])))                                     :members_only})

(def optional-from->to {#(= "Yes" (or
                                   (get-in %1 [:infobox :exchange])
                                   (get-in %1 [:infobox :exchange1]))) :exchange})

(defn mats->vec [recipe]
  (loop [counter 1
         t []]
    (let [name (get recipe (keyword (str "mat" counter)))
          quantity (get recipe (keyword (str "mat" counter "quantity")))]
      (if (nil? name)
        t
        (recur (inc counter) (conj t {:name     name
                                      :quantity (or quantity "1")}))))))

(defn skills->vec [recipe]
  (loop [counter 1
         t []]
    (let [name (get recipe (keyword (str "skill" counter)))
          level (get recipe (keyword (str "skill" counter "lvl")))
          exp (get recipe (keyword (str "skill" counter "exp")))]
      (if (nil? name)
        t
        (recur (inc counter) (conj t {:name  name
                                      :level (or level "0")
                                      :exp   (or exp "0")}))))))

(def recipe-from->to {mats->vec                         :materials
                      skills->vec                       :skills
                      #(= "Yes" (get-in %1 [:members])) :members_only
                      #(get-in %1 [:ticks])             :ticks
                      #(get-in %1 [:tools])             :tools
                      #(get-in %1 [:facilities])        :facilities})

(defn remap [mappings craftable]
  (reduce (fn [t [from-fn to]]
            (if (nil? (get t to))
              (assoc t to (from-fn craftable))
              t))
          {} mappings))

(defn remap-craftables []
  (->> (read-dir keywordize-target)
       (map (fn [craftable]
              {:meta    (merge (remap required-from->to craftable)
                               (remap optional-from->to craftable)
                               (static-vals (or (get-in craftable [:infobox :id]) (get-in craftable [:infobox :id1]))
                                            (or (get craftable :name) (get-in craftable [:infobox :name]) (get-in craftable [:infobox :name1]))))
               :recipes (reduce #(conj %1 (remap recipe-from->to %2)) [] (:recipe craftable))}))
       (map #(pr-edn transformed-target (get-in %1 [:meta :name]) %1))))

(defn do-it []
  (keywordize-craftables)
  (remap-craftables))

