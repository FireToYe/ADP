package de.schlichtherle.license;

import java.beans.DefaultPersistenceDelegate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import de.schlichtherle.xml.PersistenceService;


/**
 * 
* @ClassName: LicenseContent  
* @Description: LicenseContent内容类,系统的校验内容
* @author an48huf  
* @date 2017年11月29日  
*
 */
public class LicenseContent implements Serializable, Cloneable {

	static {

		PersistenceService.setPersistenceDelegate(X500Principal.class,

				new DefaultPersistenceDelegate(new String[] { "name" })); // NOI18N
	}

	private X500Principal holder;
	private X500Principal issuer;
	private String subject;
	private Date issued;
	private Date notBefore;
	private Date notAfter;
	private String consumerType;
	/**
	 * 一级组织架构数量
	 */
	private int consumerAmount = 21;
	private String info;
	private Object extra;
	private transient PropertyChangeSupport propertySupport;

	private int amount = 21;
	/**
	 * wms登录数量,默认20个
	 */
	private int wmsAppAmount = 20;
	/**
	 * 默认一级组织架构数量21
	 */
	public final static int DEFAULT_CONSUMERAMOUNT = 21;
	/**
	 * 默认wms登录数量,默认20个
	 */
	public final static int DEFAULT_WMSAPPAMOUNT = 20;


	protected Object clone() {
		try {
			LicenseContent localLicenseContent = (LicenseContent) super.clone();
			localLicenseContent.issued = ((Date) this.issued.clone());
			localLicenseContent.notBefore = ((Date) this.notBefore.clone());
			localLicenseContent.notAfter = ((Date) this.notAfter.clone());
			return localLicenseContent;
		} catch (CloneNotSupportedException localCloneNotSupportedException) {
			throw new AssertionError(localCloneNotSupportedException);
		}
	}

	public X500Principal getHolder() {
		return this.holder;
	}

	public synchronized void setHolder(X500Principal paramX500Principal) {
		X500Principal localX500Principal = this.holder;
		this.holder = paramX500Principal;
		firePropertyChange("holder", localX500Principal, paramX500Principal);
	}

	public X500Principal getIssuer() {
		return this.issuer;
	}

	public synchronized void setIssuer(X500Principal paramX500Principal) {
		X500Principal localX500Principal = this.issuer;
		this.issuer = paramX500Principal;
		firePropertyChange("issuer", localX500Principal, paramX500Principal);
	}

	public String getSubject() {
		return this.subject;
	}

	public synchronized void setSubject(String paramString) {
		String str = this.subject;
		this.subject = paramString;
		firePropertyChange("subject", str, paramString);
	}

	public Date getIssued() {
		return this.issued;
	}

	public void setIssued(Date paramDate) {
		Date localDate = this.issued;
		this.issued = paramDate;
		firePropertyChange("issued", localDate, paramDate);
	}

	public Date getNotBefore() {
		return this.notBefore;
	}

	public void setNotBefore(Date paramDate) {
		Date localDate = this.notBefore;
		this.notBefore = paramDate;
		firePropertyChange("notBefore", localDate, paramDate);
	}

	public Date getNotAfter() {
		return this.notAfter;
	}

	public void setNotAfter(Date paramDate) {
		Date localDate = this.notAfter;
		this.notAfter = paramDate;
		firePropertyChange("notAfter", localDate, paramDate);
	}

	public String getConsumerType() {
		return this.consumerType;
	}

	public void setConsumerType(String paramString) {
		String str = this.consumerType;
		this.consumerType = paramString;
		firePropertyChange("consumerType", str, paramString);
	}

	public int getConsumerAmount() {
		return this.consumerAmount;
	}

	public void setConsumerAmount(int paramInt) {
		int i = this.consumerAmount;
		this.consumerAmount = paramInt;
		firePropertyChange("consumerAmount", new Integer(i), new Integer(paramInt));
	}

	public int getWmsAppAmount() {
		return this.wmsAppAmount;
	}

	public void setWmsAppAmount(int wmsAppAmount) {
		int i = this.wmsAppAmount;
		this.wmsAppAmount = wmsAppAmount;
		firePropertyChange("wmsAppAmount", new Integer(i), new Integer(wmsAppAmount));
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String paramString) {
		String str = this.info;
		this.info = paramString;
		firePropertyChange("info", str, paramString);
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int paramInt) {
		int i = this.amount;
		this.amount = paramInt;
		firePropertyChange("amount", new Integer(i), new Integer(paramInt));
	}

	public Object getExtra() {
		return this.extra;
	}

	public void setExtra(Object paramObject) {
		Object localObject = this.extra;
		this.extra = paramObject;
		firePropertyChange("extra", localObject, paramObject);
	}

	public int hashCode() {
		return getConsumerAmount() + hashCode(getConsumerType()) + hashCode(getHolder()) + hashCode(getInfo())
				+ hashCode(getIssued()) + hashCode(getIssuer()) + hashCode(getNotAfter()) + hashCode(getNotBefore())
				+ hashCode(getSubject()) + hashCode(getWmsAppAmount()) + hashCode(getAmount());
	}

	private static final int hashCode(Object paramObject) {
		return paramObject != null ? paramObject.hashCode() : 0;
	}

	public boolean equals(Object paramObject) {
		if (!(paramObject instanceof LicenseContent)) {
			return false;
		}
		LicenseContent localLicenseContent = (LicenseContent) paramObject;
		return (localLicenseContent.getConsumerAmount() == getConsumerAmount())
				&& (isNullOrEquals(localLicenseContent.getConsumerType(), getConsumerType()))
				&& (isNullOrEquals(localLicenseContent.getHolder(), getHolder()))
				&& (isNullOrEquals(localLicenseContent.getInfo(), getInfo()))
				&& (isNullOrEquals(localLicenseContent.getIssued(), getIssued()))
				&& (isNullOrEquals(localLicenseContent.getIssuer(), getIssuer()))
				&& (isNullOrEquals(localLicenseContent.getNotAfter(), getNotAfter()))
				&& (isNullOrEquals(localLicenseContent.getNotBefore(), getNotBefore()))
				&& (isNullOrEquals(localLicenseContent.getSubject(), getSubject()))
				&& (isNullOrEquals(localLicenseContent.getWmsAppAmount(), getWmsAppAmount()))
				&& (isNullOrEquals(localLicenseContent.getAmount(), getAmount()));
	}

	private static final boolean isNullOrEquals(Object paramObject1, Object paramObject2) {
		return paramObject2 == null ? true : paramObject1 != null ? paramObject1.equals(paramObject2) : false;
	}

	public final synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
		if (this.propertySupport == null) {
			this.propertySupport = new PropertyChangeSupport(this);
		}
		this.propertySupport.addPropertyChangeListener(paramPropertyChangeListener);
	}

	public final synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener) {
		if (this.propertySupport == null) {
			return;
		}
		this.propertySupport.removePropertyChangeListener(paramPropertyChangeListener);
	}

	protected final void firePropertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
		if (this.propertySupport == null) {
			return;
		}
		this.propertySupport.firePropertyChange(paramPropertyChangeEvent);
	}

	protected final void firePropertyChange(String paramString, Object paramObject1, Object paramObject2) {
		if (this.propertySupport == null) {
			return;
		}
		this.propertySupport.firePropertyChange(paramString, paramObject1, paramObject2);
	}

	@Override
	public String toString() {
		return "LicenseContent [holder=" + holder + ", issuer=" + issuer + ", subject=" + subject + ", issued=" + issued
				+ ", notBefore=" + notBefore + ", notAfter=" + notAfter + ", consumerType=" + consumerType
				+ ", consumerAmount=" + consumerAmount + ", info=" + info + ", extra=" + extra + ", amount=" + amount
				+ ", wmsAppAmount=" + wmsAppAmount + "]";
	}

	public static void main(String[] args) {

	}
}
