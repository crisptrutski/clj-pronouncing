(ns com.lemonodor.pronouncing-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [com.lemonodor.pronouncing :as pronouncing]))

(deftest cmudict-test
  (testing "parse-cmudict-line"
    (is (= ["\"unquote" "AH1 N K W OW1 T"]
           (pronouncing/parse-cmudict-line "\"UNQUOTE  AH1 N K W OW1 T")))
    (is (= ["aaa" "T R IH2 P AH0 L EY1"]
           (pronouncing/parse-cmudict-line "AAA  T R IH2 P AH0 L EY1")))
    (is (= ["aaronson's" "AA1 R AH0 N S AH0 N Z"]
           (pronouncing/parse-cmudict-line
            "AARONSON'S(1)  AA1 R AH0 N S AH0 N Z")))
    (is (= ["abkhazian" "AE0 B K AE1 Z Y AH0 N"]
           (pronouncing/parse-cmudict-line
            "ABKHAZIAN(3)  AE0 B K AE1 Z Y AH0 N"))))
  (testing "parse-cmudict"
    (let [cmudict (str ";; Test dict\n"
                       "LLAMAS  L AA1 M AH0 Z\n"
                       "LOUIS'(1)  L UW1 IY0 Z\n"
                       "LOUIS'(2)  L UW1 IH0 S IH0 Z\n")
          db (pronouncing/parse-cmudict (io/reader (.getBytes cmudict)))]
      (is (= [["llamas" "L AA1 M AH0 Z"]
              ["louis'" "L UW1 IY0 Z"]
              ["louis'" "L UW1 IH0 S IH0 Z"]]
             db))))
  (testing "word-phones-map"
    (let [db (pronouncing/word-phones-map)]
      (is (= ["JH AA1 N"]
             (db "john")))
      (is (= ["L UW1 IH0 S"
              "L UW1 IY0 Z"
              "L UW1 IH0 S IH0 Z"]
             (db "louis'"))))))

(deftest phones-for-word
  (testing "phones for word"
    (is (= ["P ER0 M IH1 T"
            "P ER1 M IH2 T"]
           (pronouncing/phones-for-word "permit")))))


(deftest search
  (testing "search"
    (is (= ["all-purpose" "interpolate" "interpolated" "multipurpose"
            "perpetrate" "perpetrated" "perpetrates" "perpetrating"
            "perpetrator" "perpetrator's" "perpetrators" "proserpina"
            "purpa" "purple" "purples" "purpose" "purposeful" "purposefully"
            "purposeless" "purposely" "purposes" "purposes" "serpas" "serpent"
            "serpent's" "serpentine" "serpents" "terpening" "tirpak" "turpen"
            "turpentine"]
           (pronouncing/search "ER1 P AH0")))))


(deftest stresses
  (testing "stresses"
    (is (= "12"
           (pronouncing/stresses "P ER1 M IH2 T")))))


(deftest search-stresses
  (testing "search-stresses"
    (is (= ["G UW0 B ER2 N AH0 T AO1 R IY2 AH0 L"]
           (pronouncing/phones-for-word "gubernatorial")))

    (is (= "020120"
           (pronouncing/stresses "G UW0 B ER2 N AH0 T AO1 R IY2 AH0 L")))
    (is (= ["gubernatorial"]
           (pronouncing/search-stresses "020120")))))


(deftest stresses-for-word
  (testing "stresses-for-word"
    (is (= ["01" "12"]
           (pronouncing/stresses-for-word "permit")))))


(deftest syllable-count
  (testing "syllable-count"
    (is (= 4
           (pronouncing/syllable-count "L IH1 T ER0 AH0 L IY0")))))


(deftest syllable-count-for-word
  (testing "syllable-count-for-word"
    (is (= #{4 3}
           (pronouncing/syllable-count-for-word "literally")))))


(deftest rhyming-part
  (testing "rhyming-part"
    (is (= "ER1 P AH0 L"
           (pronouncing/rhyming-part
            ;; "purple"
            "P ER1 P AH0 L")))))


(deftest rhymes
  (testing "rhymes"
    (is (= ["commissioner" "parishioner" "petitioner" "practitioner"]
           (pronouncing/rhymes "conditioner")))))
