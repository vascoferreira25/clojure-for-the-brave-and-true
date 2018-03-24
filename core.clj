(ns fwpd-vampire-data-analysis.core
  (:gen-class))

;; Now it’s time to get your hands dirty by building up the fwpd/src/fwpd/core.clj
;; file. I recommend that you start a new REPL session so you can try things out as
;; you go along. In Emacs you can do this by opening fwpd/src/fwpd/core.clj and
;; running M-x cider-restart. Once the REPL is started, delete the contents of
;; core.clj, and then add this:

(def filename "suspects.csv")

(slurp filename)
                                        ; => "Edward Cullen,10\nBella Swan,0\nCharlie Swan,0\nJacob Black,3\nCarlisle Cullen,6"

;; If the slurp function doesn’t return the preceding string, try restarting your
;; REPL session with core.clj open.

;; Next, add this to core.clj:
(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

;; Ultimately, you’ll end up with a sequence of maps that look like {:name "Edward
;; Cullen" :glitter-index 10}, and the preceding definitions help you get there.
;; First, vamp-keys ➊ is a vector of the keys that you’ll soon use to create
;; vampire maps. Next, the function str->int ➋ converts a string to an integer. The
;; map conversions ➌ associates a conversion function with each of the vamp keys.
;; You don’t need to transform the name at all, so its conversion function is
;; identity, which just returns the argument passed to it. The glitter index is
;; converted to an integer, so its conversion function is str->int. Finally, the
;; convert function ➍ takes a vamp key and a value, and returns the converted
;; value. Here’s an example:
(convert :glitter-index "3")
                                        ; => 3

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\r\n")))

;; The parse function takes a string and first splits it on the newline character
;; to create a seq of strings. Next, it maps over the seq of strings, splitting
;; each one on the comma character. Try running parse on your CSV:
(parse (slurp filename))
                                        ; => (["Edward Cullen" "10"] ["Bella Swan" "0"] ["Charlie Swan" "0"]
                                        ; =>  ["Jacob Black" "3"] ["Carlisle Cullen" "6"])

;; The next bit of code takes the seq of vectors and combines it with your vamp
;; keys to create maps:
(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         ;; Reduce fn with {} and map vector
         (reduce (fn [row-map [vamp-key value]]
                   ;; associate a vamp key with value
                   ;; convert the string to int
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       ;; Arg for first fn
       rows))

;; In this function, map transforms each row—vectors like ["Bella Swan" 0]—into a
;; map by using reduce in a manner similar to the first example in “reduce” above.
;; First, map creates a seq of key-value pairs like ([:name "Bella
;; Swan"] [:glitter-index 0]). Then, reduce builds up a map by associating a vamp
;; key with a converted vamp value into row-map. Here’s the first row mapified:
(first (mapify (parse (slurp filename))))
                                        ; => {:glitter-index 10, :name "Edward Cullen"}

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;; This takes fully mapified vampire records and filters out those with
;; a :glitter-index less than the provided minimum-glitter:
(glitter-filter 3 (mapify (parse (slurp filename))))
                                        ; => ({:name "Edward Cullen", :glitter-index 10}
                                        ; =>  {:name "Jacob Black", :glitter-index 3}
                                        ; =>  {:name "Carlisle Cullen", :glitter-index 6})

;; Et voilà! You are now one step closer to fulfilling your dream of being a
;; supernatural-creature-hunting vigilante. You better go round up those sketchy
;; characters!

(defn glitter-filter-names
  [minimum-glitter records]
  (map :name (glitter-filter minimum-glitter records)))

(glitter-filter-names 3 (mapify (parse (slurp filename))))


(mapify (parse (slurp filename)))

(assoc {} :name "This" :glitter-index 10)

(def a-list-of-suspects
  (mapify (parse (slurp filename))))

;; 1.
(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;; option 1
(defn glitter-filter-names
  [minimum-glitter records]
  (map :name (filter #(>= (:glitter-index %) minimum-glitter) records)))

;; option 2
(defn glitter-filter-names
  [minimum-glitter records]
  (map :name (glitter-filter minimum-glitter records)))

(glitter-filter-names 3 list-of-suspects)
                                        ; => ("Edward Cullen",
                                        ; =>  "Jacob Black",
                                        ; =>  "Carlisle Cullen")

;; 2.
(defn append
  [list-of-suspects name glitter]
  (conj list-of-suspects (into {} {:name name :glitter-index glitter})))

(append list-of-suspects "George Har" 22)

;; 3.
(defn validate
  [keywords new-suspect]
  (apply = (keys new-suspect) (vals keywords)))

(validate {:keywords [:name :glitter-index]} {:name "that" :glitter-index 32})

;; 4.
(defn append-validate
  [suspects new-suspect keywords]
  (if (validate keywords new-suspect)
    (conj suspects new-suspect)
    suspects))

(append-validate list-of-suspects {:name "Awesome Guy No Glitter"} {:keywords [:name :glitter-index]})
; =>

(append-validate list-of-suspects {:name "Awesome Guy Glitter" :glitter-index 10} {:keywords [:name :glitter-index]})
