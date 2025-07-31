![Onyx Logo](https://github.com/brandmaier/onyx/blob/master/build/images/onyx-welcome.png?raw=true)

![](https://img.shields.io/github/commit-activity/m/brandmaier/onyx)
![](https://tokei.rs/b1/github/brandmaier/onyx)
![](https://img.shields.io/github/issues/brandmaier/onyx)
![Code size](https://img.shields.io/github/languages/code-size/brandmaier/onyx.svg)
![contributions](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)
<!-- badges: end -->

# Onyx-J Ωnyx日本語版

このリポジトリは[brandmaier/onyx](https://github.com/brandmaier/onyx)を日本語化したフォークです。
本リポジトリではメニューやメッセージ等を日本語化しています。以下は、オリジナルレポジトリのReadMeの和訳です。

# 概要

Onyxは、構造方程式モデリング（SEM）の作成と推定のためのフリーソフトウェア環境です。
直感的にモデルを作成できるグラフィカルユーザーインターフェースと、パラメータの最尤推定を実行する強力なバックエンドを備えています。Onyxで作成したパス図は、OpenMx、lavaan、Mplus形式にエクスポートでき、スクリプトベースのSEMソフトウェアへの移行も容易です。OnyxはTimo von Oertzenと[Andreas M. Brandmaier](https://www.brandmaier.de)によって開発され、Apache 2.0ライセンスのもとで無償公開されています。

# ダウンロード

Onyxの最新（不安定）バージョンは、このリポジトリからダウンロードできます。最新版は自動的かつ定期的にビルドされ、[こちら](https://github.com/sbtseiji/onyx-j/tree/master/dist)に配置されています。JARファイルをダウンロードして実行してください。なお、Onyxの利用にはOpenJDKなどの最新のJAVA実行環境が必要です。

# ビルド

このセクションは開発者向けです。Onyxをユーザーとして利用するだけであれば、この情報は無視して構いません。もしOnyxをソースからビルドしたい場合は、ソースコードをダウンロードし、Java開発キット（Javaコンパイラを含む）とApache Antビルドツールを用意してください。その上で、ソースコードを次のコマンドでコンパイルします。


```{bash}
ant compile
````

次に、すべてのコンパイル済みクラスを1つのjarファイルにまとめるには、以下のコマンドを実行してください。

```{bash}
ant dist
```

これにより、`dist`サブフォルダ内に`onyx.jar`というファイルが作成されます。

# 実行

jarファイルが正常に作成できたら、コマンドラインから実行できます。

```{bash}
cd dist
java -jar onyx.jar
```

# ライセンス

OnyxはApache 2.0ライセンスのもとで提供されています。

Onyxは、以下のような（改変されていない）各種ライブラリを利用しており、それぞれ元のライセンスのもとで再配布されています。

-  Erich Seifertによるvectorgraphics2d（LGPLライセンス）
-  Google IncによるDiff Match and Patch（Apache License 2.0）
-  Erich SeifertによるGRAL（LGPLライセンス）
-  Thizzerによるjtouchbar（MITライセンス）
-  junit.orgによるjUnit（Eclipse Public License 1.0）

さらに、Onyxは以下のリソースも利用しています。

-  https://icons.mono.company/ のmonoアイコン（MITライセンス）

