package com.jstarcraft.recommendation.evaluator.ranking;

import java.util.Collection;
import java.util.List;

import com.jstarcraft.ai.math.structure.matrix.SparseMatrix;
import com.jstarcraft.core.utility.KeyValue;
import com.jstarcraft.recommendation.evaluator.RankingEvaluator;

/**
 * NoveltyEvaluator
 *
 * @author Birdy
 */
public class NoveltyEvaluator extends RankingEvaluator {

	private int numberOfUsers;

	private int[] itemCounts;

	public NoveltyEvaluator(int size, SparseMatrix dataMatrix) {
		super(size);
		// use the purchase counts of the train and test data set
		numberOfUsers = dataMatrix.getRowSize();
		int numberOfItems = dataMatrix.getColumnSize();
		itemCounts = new int[numberOfItems];
		for (int itemIndex = 0; itemIndex < numberOfItems; itemIndex++) {
			itemCounts[itemIndex] = dataMatrix.getColumnScope(itemIndex);
		}
	}

	/**
	 * Evaluate on the test set with the the list of recommended items.
	 *
	 * @param testMatrix
	 *            the given test set
	 * @param recommendedList
	 *            the list of recommended items
	 * @return evaluate result
	 */
	@Override
	protected float measure(Collection<Integer> checkCollection, List<KeyValue<Integer, Float>> recommendList) {
		if (recommendList.size() > size) {
			recommendList = recommendList.subList(0, size);
		}

		float sum = 0F;
		for (KeyValue<Integer, Float> keyValue : recommendList) {
			int itemIndex = keyValue.getKey();
			int count = itemCounts[itemIndex];
			if (count > 0) {
				float probability = ((float) count) / numberOfUsers;
				float entropy = (float) -Math.log(probability);
				sum += entropy;
			}
		}
		return (float) (sum / Math.log(2F));
	}

}
