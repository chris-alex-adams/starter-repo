package au.com.rmcc.common.model;

import java.awt.image.BufferedImage;

import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

import au.com.rmcc.common.util.CoinUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManifestCoin {
    @Id
    public String id;
	private String symbol;
	private String name;
	private String image;
    private double totalSupply;
    private String description;
    private double circulatingSupply;
    private double firstSeen;
    private double marketCap;
    private String iconImage;
    private String iconType;
    private boolean isConfirmedSupply;
    private int rank;
	
	public BufferedImage getBufferedImage() {
		BufferedImage image = null;
		image = CoinUtil.decodeBase64ToBufferedImage(this.image);
		return image;
	}
	
	public static ManifestCoin merge(ManifestCoin oldCoin, ManifestCoin newCoin) {
		ManifestCoin maniCoin = new ManifestCoin();
		if(!StringUtils.isEmpty(oldCoin.getId())) {
			maniCoin.setId(oldCoin.getId());
		} else {
			maniCoin.setId(newCoin.getId());
		}
		
		if(!StringUtils.isEmpty(oldCoin.getSymbol())) {
			maniCoin.setSymbol(oldCoin.getSymbol());
		} else {
			maniCoin.setSymbol(newCoin.getSymbol());
		}
		
		if(!StringUtils.isEmpty(oldCoin.getName())) {
			maniCoin.setName(oldCoin.getName());
		} else {
			maniCoin.setName(newCoin.getName());
		}
		
		if(!StringUtils.isEmpty(oldCoin.getImage())) {
			maniCoin.setImage(oldCoin.getImage());
		} else {
			maniCoin.setImage(newCoin.getImage());
		}
		
		if(!StringUtils.isEmpty(oldCoin.getDescription())) {
			maniCoin.setDescription(oldCoin.getDescription());
		} else {
			maniCoin.setDescription(newCoin.getDescription());
		}
		
		if(oldCoin.getCirculatingSupply() != 0.0) {
			maniCoin.setCirculatingSupply(oldCoin.getCirculatingSupply());
		} else {
			maniCoin.setCirculatingSupply(newCoin.getCirculatingSupply());
		}
		
		if(oldCoin.getFirstSeen() != 0.0) {
			maniCoin.setFirstSeen(oldCoin.getFirstSeen());
		} else {
			maniCoin.setFirstSeen(newCoin.getFirstSeen());
		}
		
		if(!StringUtils.isEmpty(oldCoin.getIconImage())) {
			maniCoin.setIconImage(oldCoin.getIconImage());
		} else {
			maniCoin.setIconImage(newCoin.getIconImage());
		}
		
		if(!StringUtils.isEmpty(oldCoin.getIconType())) {
			maniCoin.setIconType(oldCoin.getIconType());
		} else {
			maniCoin.setIconType(newCoin.getIconType());
		}
		
		maniCoin.setConfirmedSupply(newCoin.isConfirmedSupply());
		maniCoin.setTotalSupply(newCoin.getTotalSupply());
		maniCoin.setMarketCap(newCoin.getMarketCap());
		maniCoin.setRank(newCoin.getRank());
		
		return maniCoin;
	}
}
