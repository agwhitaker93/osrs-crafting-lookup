(ns craftables
  (:require [clojure.java.io :as io]
            [clojure.walk :refer [keywordize-keys]])
  (:import (java.io File)))

(def source "resources/craftable")

(def keywordize-target "resources/keywordized-craftable")

(def transformed-target "resources/transformed-craftable")

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
  (spit (str target "/" filename ".edn") (pr-str content)))

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

(defn static-vals [id]
  {:ge-value   "0"
   :icon       (str base-icon-url id)
   :icon-large (str base-icon-large-url id)})

(def required [:id :name :examine :tradable :value :members-only])

(defn remap-get-in [target]
  #(get-in %1 target))

; Must be in format compatible with get-in
(def required-from->to {(remap-get-in [:infobox :id])        :id
                        (remap-get-in [:infobox :id1])       :id
                        (remap-get-in [:infobox :name])      :name
                        (remap-get-in [:infobox :name1])     :name
                        (remap-get-in [:infobox :examine])   :examine
                        (remap-get-in [:infobox :examine1])  :examine
                        (remap-get-in [:infobox :tradeable]) :tradable
                        (remap-get-in [:infobox :value])     :value
                        (remap-get-in [:infobox :members])   :members-only
                        (remap-get-in [:infobox :members1])  :members-only})

(def optional-from->to {(remap-get-in [:infobox :exchange]) :exchange})

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

(def recipe-from->to {mats->vec                    :materials
                      skills->vec                  :skills
                      (remap-get-in [:members])    :members-only
                      (remap-get-in [:ticks])      :ticks
                      (remap-get-in [:tools])      :tools
                      (remap-get-in [:facilities]) :facilities})

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
                               (static-vals (get-in craftable [:infobox :id])))
               :recipes (reduce #(conj %1 (remap recipe-from->to %2)) [] (:recipe craftable))}))
       (map #(pr-edn transformed-target (get-in %1 [:meta :name]) %1))))

(defn do-it []
  (keywordize-craftables)
  (remap-craftables))

