Pulled from https://github.com/navnorth/LRJavaLib

Then changes made for compatibility. Notably:
 * fixing encoding issues by adding "UTF-8" to the StreamEntity constructor
 * debugging changes that add dumping of the original BouncyCastle exceptions
 * removing the BouncyCastle security provider so that after the app is redeployed, it can add its own provider