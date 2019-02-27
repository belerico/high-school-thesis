package geom;

public class Point{

	private double x;
	private double y;
	/**
	 * Costruisce un nuovo oggetto di classe Point.
	 * Le coordinate sono impostate entrambe a 0
	 */
	public Point(){
		this.x = 0.0;
		this.y = 0.0;
	}
	/**
	 * Costruisce un nuovo oggetto di classe Point, inizializzando la coordinata x.
	 * <p> La coordinata x viene inizializzata dal parametro di tipo double x.
	 * <p> La coordinata y viene inizializzata a 0
	 * @param x ascissa del punto.
	 */
	public Point(double x){
		this.x = x;
		this.y = 0.0;
	}
	/**
	 * Costruisce un nuovo oggetto di classe Point, inizializzando entrambe le coordinate.
	 * <p> La coordinata x viene inizializzata dal parametro di tipo double x.
	 * <p> La coordinata y viene inizializzata dal parametro di tipo double y.
	 * @param x ascissa del punto.
	 * @param y ordinata del punto.
	 */
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	/**
	 * Modifica l'ascissa del punto.
	 * <p> La coordinata x viene modificata dal parametro di tipo double x.
	 * @param x nuova ascissa del punto.
	 */
	public void setX(double x){
		this.x = x;
	}
	/**
	 * Modifica l'ordinata del punto.
	 * <p> La coordinata y viene modificata dal parametro di tipo double y.
	 * @param y nuova ordinata del punto.
	 */
	public void setY(double y){
		this.y = y;
	}
	/**
	 * Modifica l'ascissa e l'ordinata del punto.
	 * <p> La coordinata x viene modificata dal parametro di tipo double x.
	 * <p> La coordinata y viene modificata dal parametro di tipo double y.
	 * @param x nuova ascissa del punto.
	 * @param y nuova ordinata del punto.
	 */
	public void setXY(double x, double y){
		this.x = x;
		this.y = y;
	}
	/**
	 * Ritorna l'ascissa del punto.
	 * @return l'ascissa del punto
	 */
    public double getX(){
    	return this.x;
   	}
   	/**
	 * Ritorna l'ordinata del punto.
	 * @return l'ordinata del punto
	 */
	public double getY(){
		return this.y;
	}
	/**
	 * Trasla il punto sull'asse delle x.
	 * <p> Il punto viene traslato di un valore x.
	 * <p> Se ad esempio avessimo un punto p1(2,3), il metodo p1.moveByX(4) sommerebbe alla coordinata x il valore 4; le coordinate del punto p1 diventeranno (6,3).
	 * @param x valore da sommare alla coordinata x.
	 */
	public void moveByX(double x){
		this.x += x;
	}
	/**
	 * Trasla il punto sull'asse delle y.
	 * <p> Il punto viene traslato di un valore y.
	 * <p> Se ad esempio avessimo un punto p1(2,3), il metodo p1.moveByY(4) sommerebbe alla coordinata y il valore 4; le coordinate del punto p1 diventeranno (2,7).
	 * @param y valore da sommare alla coordinata y.
	 */
	public void moveByY(double y){
		this.y += y;
	}
	/**
	 * Trasla il punto rispetto sia all'asse delle x sia all'asse delle y.
	 * <p> Il punto viene traslato sull'asse delle x di un valore x e sull'asse delle y di un valore y.
	 * <p> Se ad esempio avessimo un punto p1(2,3), il metodo p1.moveByXY(4,1) sommerebbe alla coordinata x il valore 4 e alla coordinata y il valore 4.
	 * <p> Le coordinate del punto p1 diventeranno (6,4).
	 * @param x valore da sommare alla coordinata x
	 * @param y valore da sommare alla coordinata y.
	 */
	public void moveByXY(double x, double y){
		this.x += x;
		this.y += y;
	}
	/**
	 * Ruota il punto rispetto all'origine degli assi.
	 * <p> Il punto viene ruotato rispetto all'origine degli assi di un angolo di rotazione specificato dal parametro angle.
	 * @param angle valore dell'angolo di rotazione in gradi.
	 * @return oggetto di classe Point con le coordinate x e y ruotate.
	 */
	public Point rotate(double angle){
		return rotate(new Point(0,0), angle);
	}
	/**
	 * Ruota il punto rispetto ad un punto di pivot.
	 * <p> Il punto viene ruotato rispetto al punto pivotPoint di un angolo di rotazione specificato dal parametro angle, espresso in gradi.
	 * @param pivotPoint punto di rotazione rispetto a cui ruotare il punto.
	 * @param angle valore dell'angolo di rotazione in gradi.
	 * @return oggetto di classe Point con le coordinate x e y ruotate.
	 */
	public Point rotate(Point pivotPoint, double angle){
		double x = pivotPoint.getX() + (this.x - pivotPoint.getX()) * Math.cos(Math.toRadians(angle)) - (this.y - pivotPoint.getY()) * Math.sin(Math.toRadians(angle));
		double y = pivotPoint.getY() + (this.y - pivotPoint.getY()) * Math.cos(Math.toRadians(angle)) + (this.x - pivotPoint.getX()) * Math.sin(Math.toRadians(angle));
		return new Point(Math.round(x), Math.round(y));
	}
	/**
	 * Ritorna il punto medio tra due punti.
	 * @param p oggetto di classe Point.
	 * @return oggetto di classe Point contenente le coordinate del punto medio
	 */
	public Point getMediumPoint(Point p){
		return new Point(((this.x + p.x) / 2), ((this.y + p.y) / 2));
	}
	/**
	 * Metodo statico che ritorna il punto medio tra due punti.
	 * @param p1 oggetto di classe Point.
	 * @param p2 oggetto di classe Point.
	 * @return oggetto di classe Point contenente le coordinate del punto medio
	 */
	public static Point getMediumPoint(Point p1, Point p2){
		return new Point(((p1.x + p2.x) / 2), ((p1.y + p2.y) / 2));
	}
	/**
	 * Calcola la distanza tra due punti.
	 * @param p oggetto di classe Point.
	 * @return distanza tra due punti.
	 */
	public double distance(Point p){
		return Math.sqrt((Math.pow((p.x - this.x), 2)) + (Math.pow((p.y - this.y), 2)));
	}
	/**
	 * Metodo statico che calcola la distanza tra due punti.
	 * @param p1 oggetto di classe Point.
	 * @param p2 oggetto di classe Point.
	 * @return distanza tra due punti.
	 */
	public static double distance(Point p1, Point p2){
		return Math.sqrt((Math.pow((p2.x - p1.x), 2)) + (Math.pow((p2.y - p1.y), 2)));
	}
	/**
	 * Equals deep inside che confronta le coordinate di un oggetto di classe Point con le coordinate di un altro oggetto di classe Point
	 * @param p oggetto di classe Point.
	 * @return true se le entrambe le coordinate dei due punti sono uguali.
	 */
	public boolean equals(Point p){
		return (this.x == p.x && this.y == p.y);
	}
	/**
	 * Copy deep inside che crea un nuovo oggetto di classe Point con le stesse coordinate.
	 * @return new Point() con le stesse coordinate.
	 */
	public Point copy(){
		return new Point(this.x, this.y);
	}
	/**
	 * Override del metodo toString() della classe Object.
	 * @return coordinate dell'oggetto.
	 */
	public String toString(){
		return "(" + this.x + "; " + this.y + ")";
	}
}